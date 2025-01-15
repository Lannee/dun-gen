package backend.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.exceptions.FailedRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratorService {
    private static final String POST_URL = "http://ollama:11434/api/generate";

    private String sendPOST(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String jsonInputString = String.format("{\"model\": \"llama3.2\", \"prompt\": \"%s\", \"stream\": false}", prompt);
        System.out.println("request data = " + jsonInputString);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(POST_URL))
            .header("Content-Type", "application/json; charset=UTF-8")
            .POST(BodyPublishers.ofString(jsonInputString))
            .build();

        // Send the request and get the response
        HttpResponse<String> response;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Print the response status code and body
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        String responseText = jsonNode.get("response").asText();

        return responseText;

        // URL url = new URL(POST_URL);
        // HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // con.setRequestProperty("Content-Type", "application/json;");
        // con.setConnectTimeout(20000);
        // con.setReadTimeout(20000);
        // con.setDoOutput(true); // Ensure doOutput is true for POST requests
        // System.out.println("Настроено");

        // // Write JSON payload to output stream
        // byte[] postData = jsonInputString.getBytes(StandardCharsets.UTF_8);

        // try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
        //     wr.write(postData);
        // }
        // System.out.println("Отправлено");
     
        // try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
        //     String inputLine;
        //     while ((inputLine = in.readLine()) != null) {
        //         System.out.println(inputLine);
        //     }
        // }

        // Print result
        // return response.toString();
        // return "";
    }
    
    public String generateText(String request) throws IOException, FailedRequest, InterruptedException {
        return sendPOST(request);
    }
}
