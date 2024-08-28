package com.example.photocontest.external.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailValidator {

    private static final String API_KEY = "cb33b6a98e7343bdac345a6791c276a4";
    private static final String BASE_URL = "https://api.zerobounce.net/v2/validate";

    public static boolean isEmailValid(String email) {
        String url = BASE_URL + "?api_key=" + API_KEY + "&email=" + email;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                System.out.println("Response JSON: " + json);  // Debugging
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);
                String status = root.path("status").asText();

                return "valid".equalsIgnoreCase(status) || "catch-all".equalsIgnoreCase(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}