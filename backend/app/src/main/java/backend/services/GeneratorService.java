package backend.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import backend.exceptions.FailedRequest;
import backend.model.Character;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @Configuration
// @ConfigurationProperties(prefix = "prompts")
public class GeneratorService {
    private static final String POST_URL = "http://localhost:11434/api/generate";

    // @Value("${character-json-format}")
    private final String characterJsonFormat = """
        {
            name: "",
            class_name: "",
            background: "",
            alignment: "",
            ability_scores: {
                strength: 1,
                dexterity: 1,
                constitution: 1,
                intelligence: 1,
                wisdom: 1,
                charisma: 1
            },
            skills: {
                acrobatics: 1,
                athletics: 1,
                insight: 1,
                intimidation: 1,
                nature: 1,
                perception: 1,
                survival: 1
            },
            equipment: [
                {
                    name: "",
                    description: ""
                },
                ...
            ],
            features_and_traits: [
                {
                    name: "",
                    description: ""
                },
                ...
            ],
            personality: {
                traits: [
                    "", 
                    ...
                ],
                backstory: ""
            },
            goals: [
                "", 
                ...
            ]
        }
    """;
    
    private final String character_format_explanation = """
        name: The character's name.
        class_name: The character's class (e.g., Warrior, Mage).
        background: The character's background (e.g., Noble, Outlander).
        alignment: The character's moral alignment (e.g., Lawful Good).
        ability_scores: An object containing ability scores such as strength, dexterity, etc.
        skills: An object containing skill proficiency levels.
        equipment: An array of objects representing the character's equipment.
        features_and_traits: An array of objects detailing special features and traits.
        personality: An object containing personality traits and backstory.
        goals: An array of strings representing the character's goals.
    """;

    private final String use_preset_prompt = """
        Use the folowing description as a base for generation: {0}
    """;

    private final String generate_base = """
        generate Dungeons and Dragons 5 edition character as a json sample with the following scheme:
        where: {0}

        {1}

        {2}
        respond just with generated json, no extra comments needed. Please
    """;;

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

    public Character generateCharacter() throws IOException, FailedRequest {
        return generateCharacter("");
    }

    public Character generateCharacter(String description) 
        throws IOException, FailedRequest // TODO: Remove this throw declarations from all generate methods
        {
        String prompt = getFinalGeneratePrompt(description);

        String response = generateText(prompt);
        return Character.fromJson(response);
    }
    
    protected String getFinalGeneratePrompt(String description) {
        description = description.trim();

        return MessageFormat.format(generate_base, 
            /* {0} */ character_format_explanation,
            /* {1} */ characterJsonFormat,
            /* {2} */ description.isBlank() ? "" : MessageFormat.format(use_preset_prompt, description)
        );
    }
}
