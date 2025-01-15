package backend.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.exceptions.FailedRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratorService {
    private MinioService minioService;

    private static final String POST_URL = "http://ollama:11434/api/generate";
    private static final String STABILITY_API_URL = "https://api.stability.ai/v2beta/stable-image/generate/sd3";
    private static final String AUTHORIZATION_TOKEN = "Bearer TOKEN";

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
    }
    
    public String generateText(String request) throws IOException, FailedRequest, InterruptedException {
        return sendPOST(request);
    }

    private static String generateImage(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STABILITY_API_URL))
                .header("Authorization", AUTHORIZATION_TOKEN)
                .header("Accept", "image/*")
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofString(
                        "--" + boundary + CRLF +
                        "Content-Disposition: form-data; name=\"prompt\"" + CRLF +
                        CRLF +
                        prompt + CRLF +
                        "--" + boundary + CRLF +
                        "Content-Disposition: form-data; name=\"output_format\"" + CRLF +
                        CRLF +
                        "jpeg" + CRLF +
                        "--" + boundary + "--" + CRLF
                ))
                .build();

        HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());

        if (response.statusCode() == 200) {
            // Save image locally
            File outputFile = new File("./lighthouse.jpeg");
            try (InputStream inputStream = response.body();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return outputFile.getAbsolutePath();
        }

        return "Failed to generate hero apperance image.";
    }

    public String getGeneratedImage(String prompt) {
        // return minioService.putObject("", new LinkedList<>().add(generateImage(prompt)));
    }
}
