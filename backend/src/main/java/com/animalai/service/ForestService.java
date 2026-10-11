
package com.animalai.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForestService {

    private final RestTemplate restTemplate;
    private final String modelUrl;

    public ForestService(
            RestTemplate restTemplate,
            @Value("${ml.model2.base-url}") String modelUrl) {
        this.restTemplate = restTemplate;
        this.modelUrl = modelUrl;
    }

    public Map<String, Object> getStatus() {
        @SuppressWarnings("unchecked")
        Map<String, Object> result = restTemplate.getForObject(
                modelUrl + "/status",
                Map.class
        );

        if (result == null || !result.containsKey("status")) {
            throw new IllegalStateException(
                    "Model 2 returned an unexpected status response");
        }

        return result;
    }

    public Map<String, Object> updateStatus(
            Map<String, Object> payload) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(payload, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> result = restTemplate.postForObject(
                modelUrl + "/status",
                request,
                Map.class
        );

        return result;
    }
}