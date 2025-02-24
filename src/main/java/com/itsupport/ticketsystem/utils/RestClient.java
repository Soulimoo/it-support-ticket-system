package com.itsupport.ticketsystem.utils;

import com.itsupport.ticketsystem.ui.LoginUI;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestClient {
    private static final String BASE_URL = "http://localhost:8080/";

    public static String sendPostRequest(String endpoint, String jsonPayload) {
        try {
            String urlString = "http://localhost:8080/" + endpoint;
            System.out.println("Connecting to API at: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            if (LoginUI.getLoggedInUsername() != null && LoginUI.getLoggedInPassword() != null) {
                String auth = LoginUI.getLoggedInUsername() + ":" + LoginUI.getLoggedInPassword();
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            }

            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response Data: " + response.toString());
                    return response.toString();
                }
            } else {
                System.out.println("Error: " + responseCode);
                return "{}";
            }
        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
            return "{}";
        }
    }

    public static String sendGetRequest(String endpoint) {
        try {
            String urlString = "http://localhost:8080/" + endpoint;
            System.out.println("Connecting to API at: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            if (LoginUI.getLoggedInUsername() != null && LoginUI.getLoggedInPassword() != null) {
                String auth = LoginUI.getLoggedInUsername() + ":" + LoginUI.getLoggedInPassword();
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response Data: " + response.toString());
                    return response.toString();
                }
            } else {
                System.out.println("Error: " + responseCode);
                return "{}";
            }
        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
            return "{}";
        }
    }

    private static String encodeCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    private static String getResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        BufferedReader reader;

        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static String sendPatchRequest(String endpoint, String jsonPayload) {
        try {
            String url = BASE_URL + endpoint;
            System.out.println("Connecting to API at: " + url);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (LoginUI.getLoggedInUsername() != null && LoginUI.getLoggedInPassword() != null) {
                String auth = LoginUI.getLoggedInUsername() + ":" + LoginUI.getLoggedInPassword();
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                headers.set("Authorization", "Basic " + encodedAuth);
            }

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

            System.out.println("Response Code: " + response.getStatusCodeValue());
            System.out.println("Response Data: " + response.getBody());

            return response.getBody();
        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
            return "{}";
        }
    }
}
