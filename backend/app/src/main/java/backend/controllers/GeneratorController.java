package backend.controllers;

import java.io.IOException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.DTO.GeneratorDTO;
import backend.model.validators.TokenValidator;
import backend.model.Character;
import backend.security.JwtUtils;
import backend.services.GeneratorService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/gen", produces = { "application/json" })
public class GeneratorController {
    private final JwtUtils jwtUtils;
    private final GeneratorService generatorService;

    @PostMapping(path = "/text")
    public ResponseEntity<?> generateText(@RequestBody GeneratorDTO req) throws NotFoundException, IOException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        return ControllerExecutor.execute(validator, () -> {
            String response = generatorService.generateText(req.getPrompt());

            return ResponseEntity.ok().body(response);
        });
    }

    @PostMapping(path = "/character")
    public ResponseEntity<?> generateCharacter(@RequestBody GeneratorDTO req) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        return ControllerExecutor.execute(validator, () -> {
            Character response = generatorService.generateCharacter(req.getPrompt());

            return ResponseEntity.ok().body(response);
        });
    }
}
