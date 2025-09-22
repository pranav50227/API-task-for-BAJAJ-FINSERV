package com.BAJAJ.API_ROUND;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class TestService {

    private final WebClient webClient = WebClient.create();

    public Map<String, Object> generateWebhook() {
        Map<String, String> requestBody = Map.of(
                "name", "Pranav Yadav",
                "regNo", "0101CS221095",
                "email", "pranav.2001y@gmail.com"
        );

        return webClient.post()
                .uri("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public String getSqlQuery() {
        return "SELECT p.AMOUNT as SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) as AGE, " +
                "d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) != 1 " +
                "ORDER BY p.AMOUNT DESC " +
                "LIMIT 1";
    }

    public String submitSolution(String webhook, String accessToken) {
        Map<String, String> submitBody = Map.of("finalQuery", getSqlQuery());

        return webClient.post()
                .uri(webhook)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(submitBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String executeFullFlow() {
        Map<String, Object> webhookResponse = generateWebhook();
        String webhook = (String) webhookResponse.get("webhook");
        String accessToken = (String) webhookResponse.get("accessToken");
        return submitSolution(webhook, accessToken);
    }
}