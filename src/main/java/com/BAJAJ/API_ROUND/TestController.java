package com.BAJAJ.API_ROUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/webhook")
    public ResponseEntity<?> testWebhookGeneration() {
        try {
            Map<String, Object> result = testService.generateWebhook();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/sql")
    public ResponseEntity<?> getSqlQuery() {
        String sqlQuery = testService.getSqlQuery();
        return ResponseEntity.ok(Map.of("finalQuery", sqlQuery));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> testSubmission(@RequestBody Map<String, String> request) {
        try {
            String webhook = request.get("webhook");
            String accessToken = request.get("accessToken");
            String result = testService.submitSolution(webhook, accessToken);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/full-flow")
    public ResponseEntity<?> testFullFlow() {
        try {
            String result = testService.executeFullFlow();
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "API_ROUND"));
    }
}