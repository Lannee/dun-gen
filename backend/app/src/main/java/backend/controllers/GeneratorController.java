package backend.controllers;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.DTO.ChapterDTO;
import backend.DTO.GeneratorDTO;
import backend.model.Chapter;
import backend.model.validators.TokenValidator;
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

    @PostMapping(path = "/text")
    public ResponseEntity<?> generateText(@RequestBody GeneratorDTO req) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        return ControllerExecutor.execute(validator, () -> {
            String response = generatorService.generateText(req.getPrompt());

            return ResponseEntity.ok().body(response);
        });
    }
}
