
package com.animalai.controller;

import java.util.Map;

import com.animalai.service.FootprintService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/footprints")
public class FootprintController {

    private final FootprintService service;

    public FootprintController(FootprintService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/predict",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> predict(
            @RequestParam("file") MultipartFile file)
            throws Exception {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Please upload an image")
            );
        }

        if (file.getContentType() == null
                || !file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "The uploaded file must be an image")
            );
        }

        return ResponseEntity.ok(service.predict(file));
    }
}