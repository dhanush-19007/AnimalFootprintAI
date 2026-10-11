
package com.animalai.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FootprintService {

    private final RestTemplate restTemplate;
    private final String modelUrl;

    public FootprintService(
            RestTemplate restTemplate,
            @Value("${ml.model1.base-url}") String modelUrl) {
        this.restTemplate = restTemplate;
        this.modelUrl = modelUrl;
    }

    public Map<String, Object> predict(MultipartFile file)
            throws Exception {

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            filename = "footprint.jpg";
        }

        final String uploadName = filename;

        ByteArrayResource resource =
                new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return uploadName;
                    }
                };

        HttpHeaders partHeaders = new HttpHeaders();
        partHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<ByteArrayResource> filePart =
                new HttpEntity<>(resource, partHeaders);

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();
        body.add("file", filePart);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> result = restTemplate.postForObject(
                modelUrl + "/predict",
                request,
                Map.class
        );

        if (result == null || !result.containsKey("animal")) {
            throw new IllegalStateException(
                    "Model 1 returned an unexpected response");
        }

        return result;
    }
}