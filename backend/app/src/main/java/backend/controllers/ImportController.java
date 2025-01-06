package backend.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import backend.DTO.ImportDTO;
import backend.DTO.TokenDTO;
import backend.exceptions.DoesNotExistException;
import backend.model.validators.TokenValidator;
import backend.security.JwtUtils;
import backend.services.ImportService;
import backend.services.MinioService;
import backend.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/import", produces = { "application/json" })
public class ImportController {
    private final JwtUtils jwtUtils;
    private final ImportService importService;
    private final UserService userService;
    private final MinioService minioService;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("token") TokenDTO token)
            throws IOException, DoesNotExistException {

        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(token.getToken());

        return ControllerExecutor.execute(validator, () -> {
            ImportDTO result = importService.processYamlFile(file, token);

            return ResponseEntity.ok().body(result);
        });
    }

    @PostMapping("/history")
    public ResponseEntity<?> getHistory(@RequestBody TokenDTO token) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(token.getToken());

        return ControllerExecutor.execute(validator, () -> {
            List<ImportDTO> result = importService.getImports(token);
        
            return ResponseEntity.ok().body(result);
        });
    }

    @PostMapping("/download/{objectName}")
    public ResponseEntity<?> downloadFile(@RequestBody TokenDTO token, @PathVariable String objectName) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(token.getToken());

        return ControllerExecutor.execute(validator, () -> {
            try {
            InputStreamResource resource = new InputStreamResource(minioService.getObject("", objectName));
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                    .body(resource);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error downlaoding file", e);
            }
        });
    }
}