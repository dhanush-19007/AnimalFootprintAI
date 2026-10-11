
package com.animalai.controller;

import java.util.Map;
import java.util.Set;

import com.animalai.service.ForestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forest")
public class ForestController {

    private static final Set<String> VALID_STATUSES =
            Set.of("SAFE", "DETECTED", "NEAR", "ALERT");

    private final ForestService service;

    public ForestController(ForestService service) {
        this.service = service;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(service.getStatus());
    }

    @PostMapping("/status")
    public ResponseEntity<?> updateStatus(
            @RequestBody Map<String, Object> payload) {

        Object rawStatus = payload.get("status");

        if (!(rawStatus instanceof String status)
                || !VALID_STATUSES.contains(status)) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Invalid status",
                            "allowed", VALID_STATUSES
                    )
            );
        }

        return ResponseEntity.ok(service.updateStatus(payload));
    }
}