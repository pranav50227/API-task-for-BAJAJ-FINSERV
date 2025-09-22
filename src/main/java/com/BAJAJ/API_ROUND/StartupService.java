package com.BAJAJ.API_ROUND;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class StartupService {

    private final WebClient webClient = WebClient.create();

    @Bean
    public ApplicationRunner run() {
        return args -> {
            generateWebhookAndSubmit();
        };
    }

    private void generateWebhookAndSubmit() {
        Map<String, String> requestBody = Map.of(
                "name", "Pranav Yadav",
                "regNo", "0101CS221095",
                "email", "pranav.2001y@gmail.com"
        );

        webClient.post()
                .uri("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    String webhook = (String) response.get("webhook");
                    String accessToken = (String) response.get("accessToken");
                    return submitSolution(webhook, accessToken);
                })
                .subscribe();
    }

    private Mono<String> submitSolution(String webhook, String accessToken) {
        String sqlQuery = "SELECT p.AMOUNT as SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) as AGE, " +
                "d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) != 1 " +
                "ORDER BY p.AMOUNT DESC " +
                "LIMIT 1";

        Map<String, String> submitBody = Map.of("finalQuery", sqlQuery);

        return webClient.post()
                .uri(webhook)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(submitBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
