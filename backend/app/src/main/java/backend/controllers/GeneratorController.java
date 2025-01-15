package backend.controllers;

import java.io.IOException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.DTO.CharacterDTO;
import backend.DTO.GeneratorDTO;
import backend.model.validators.TokenValidator;
import backend.repository.CharacterRepository;
import backend.model.Character;
import backend.model.User;
import backend.security.JwtUtils;
import backend.services.GeneratorService;
import backend.services.UserService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/gen", produces = { "application/json" })
public class GeneratorController {
    private final JwtUtils jwtUtils;
    private final GeneratorService generatorService;
    private final UserService userService;

    private final CharacterRepository characterRepository;

    @PostMapping(path = "/text")
    public ResponseEntity<?> generateText(@RequestBody GeneratorDTO req) throws NotFoundException, IOException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        return ControllerExecutor.execute(validator, () -> {
            String response = generatorService.generateText(req.getPrompt());

            return ResponseEntity.ok().body(response);
        });
    }

    @PostMapping(path = "/character/generate")
    public ResponseEntity<?> generateCharacter(@RequestBody GeneratorDTO req) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        User user = userService.getById(userId);

        return ControllerExecutor.execute(validator, () -> {
            Character character = generatorService.generateCharacter(req.getPrompt());
            character.setUser(user);

            characterRepository.save(character);

            return ResponseEntity.ok().body(character);
        });
    }

    @PostMapping(path = "/character/regenerate")
    public ResponseEntity<?> regenerateCharacter(@RequestBody CharacterDTO req) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());

        long userId = jwtUtils.getIdFromToken(req.getToken());
        User user = userService.getById(userId);

        return ControllerExecutor.execute(validator, () -> {
            Character character = generatorService.regenerateCharacter(req);
            character.setUser(user);

            characterRepository.save(character);

            return ResponseEntity.ok().body(character);
        });
    }
}