package backend.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import backend.exceptions.FailedRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratorService {
    private static final String POST_URL = "http://localhost:11434/api/generate";

    private String sendPOST(String prompt) throws IOException, FailedRequest {
        URL url = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String jsonInputString = String.format("{\"model\": \"llama3.2\", \"prompt\": \"%s\", \"stream\": false}", prompt);
        System.out.println("Constructed JSON payload: " + jsonInputString);

        // Setting the request method to POST
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Enable sending output
        con.setDoOutput(true);
        System.out.println("Connection established. Sending request...");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            System.out.println("JSON payload sent successfully.");
        }
        
        // Getting the response code
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        // Reading the response
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new FailedRequest("Failed to generate data");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print result
        return response.toString();
    }
    
    public String generateText(String request) throws IOException, FailedRequest {
        String result = sendPOST(request);

        return result;
    }
}
