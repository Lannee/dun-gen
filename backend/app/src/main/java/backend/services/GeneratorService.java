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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.DTO.CharacterDTO;
import backend.exceptions.FailedRequest;
import backend.model.Character;
import backend.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @Configuration
// @ConfigurationProperties(prefix = "prompts")
public class GeneratorService {
    private static final String POST_URL = "http://ollama:11434/api/generate";

    private final String characterJsonFormat = "{name: '', level: 1, experience: 0, class_name: '', race: '', background: '', alignment: '', ability_scores: {strength: 1, dexterity: 1, constitution: 1, intelligence: 1, wisdom: 1, charisma: 1}, skills: {acrobatics: 1, athletics: 1, insight: 1, intimidation: 1, nature: 1, perception: 1, survival: 1}, equipment: [{name: '', description: ''}, ...], features_and_traits: [{name: '', description: ''}, ...], personality: {traits: ['', ...], backstory: ''}, goals: ['', ...]}";
    
    private final String character_format_explanation = "name: The character's name. level: The character's current level. experience: The character's current experience (must correspond to level).  class_name: The character's class (e.g., Warrior, Mage). race: The character's race (e.g., Elf, Dwarf, Human). background: The character's background (e.g., Noble, Outlander). alignment: The character's moral alignment (e.g., Lawful Good). ability_scores: An object containing ability scores such as strength, dexterity, etc. skills: An object containing skill proficiency levels. equipment: An array of objects representing the character's equipment. features_and_traits: An array of objects detailing special features and traits. personality: An object containing personality traits and backstory. goals: An array of strings representing the character's goals.";

    private final String use_preset_prompt = "Use the folowing description as a base for generation: {0}";

    private final String generate_base = "generate detailed level appropriate (with large backstory) Dungeons and Dragons 5 edition character description (add spells, weapons, items, skills and other things he could have) as a json sample with the following scheme: {0} where: {1} {2} Respond just with generated json, no extra comments needed. Please";


    private final String regenerate_base = "regenerate Dungeons and Dragons 5 edition character description according to what have changed using some rules: Use the same json format. Changes must correspond with dnd 5e rules. If Level has changed recount all numeric abilities and skills according to new value. If class or rase have changes recount all numeric characteristics features and traits according to new values. If alignmet or background have changed and at the same time backstory was edited, rewrite backstory according to new alignmet or background. All other fields (equipment, goals, and other) bring from new version. Old version: {0}. New version (changes): {1}. Respond just with generated json, no extra comments needed. Please";

    private final CharacterRepository characterRepository;

    private String sendPOST(String prompt) throws IOException, FailedRequest, InterruptedException {
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

    public Character generateCharacter() throws IOException, FailedRequest, InterruptedException {
        return generateCharacter("");
    }

    public Character generateCharacter(String description) 
        throws IOException, FailedRequest // TODO: Remove this throw declarations from all generate methods
        , InterruptedException
        {
        String prompt = getFinalGeneratePrompt(description);

        String response = generateText(prompt);
        Character character = Character.fromJson(response);
        return character;
    }
    
    protected String getFinalGeneratePrompt(String description) {
        description = description.trim().replaceAll("\r", "").replaceAll("\n", "");

        return MessageFormat.format(generate_base, 
            /* {0} */ character_format_explanation,
            /* {1} */ characterJsonFormat,
            /* {2} */ description.isBlank() ? "" : MessageFormat.format(use_preset_prompt, description)
        );
    }


    public Character regenerateCharacter(CharacterDTO changes) 
        throws IOException, FailedRequest // TODO: Remove this throw declarations from all generate methods
        , InterruptedException
        {
        Character oldCharacter = characterRepository.getReferenceById(changes.getId());

        String prompt = getFinalRegeneratePrompt(changes, oldCharacter.toDTO());

        System.out.println(prompt);

        String response = generateText(prompt);
        Character character = Character.fromJson(response);

        character.setId(oldCharacter.getId());

        return character;
    }

    protected String getFinalRegeneratePrompt(CharacterDTO changes, CharacterDTO oldVersion) {
        return MessageFormat.format(regenerate_base, 
            /* {0} */ oldVersion.toJson(),
            /* {1} */ changes.toJson()
        );
    }
}
