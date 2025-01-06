package backend.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import backend.DTO.ImportDTO;
import backend.DTO.ChapterDTO;
import backend.DTO.SpaceMarineDTO;
import backend.DTO.TokenDTO;
import backend.configuration.MinioConfig;
import backend.exceptions.DoesNotExistException;
import backend.model.Import;
import backend.model.ImportStatus;
import backend.model.SpaceMarine;
import backend.model.AstartesCategory;
import backend.model.Chapter;
import backend.model.Coordinates;
import backend.model.User;
import backend.model.Weapon;
import backend.model.Import;
import backend.repository.ImportRepository;
import backend.repository.UserRepository;
import backend.security.JwtUtils;
import backend.utils.Pair;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportService {

    private final JwtUtils jwtUtils;
    private final AdminService adminService;
    private final UserRepository userRepository;

    private final ImportRepository importRepository;

    private final SpaceMarineService spaceMarineService;
    private final ChapterService chapterService;
    private final HistoryService historyService;

    @Autowired
    private final MinioService minioService;
    private final MinioConfig minioProperties;


    public List<ImportDTO> getImports(TokenDTO token) throws DoesNotExistException {
        final long user_id = jwtUtils.getIdFromToken(token.getToken());
        final boolean isAdmin = adminService.isAdmin(user_id);

        final User owner = userRepository.getReferenceById(user_id);
        String username = owner.getName();
        
        List<Import> impList = importRepository.findAll();
        List<ImportDTO> result = new LinkedList<>(); 
        for (Import imp : impList) {
            result.add(imp.getCreatedImport());
        }

        if (!isAdmin) {
            result.removeIf(imp -> !imp.getUserName().equals(username));
        }

        return result;
    }

    private long saveChapter(Map<String, Object> chapter, String username, TokenDTO token) throws DoesNotExistException {
        final String name = (String) chapter.get("name");
        final long marinesCount = Long.valueOf((Integer) chapter.get("marinesCount"));
        final String world = (String) chapter.get("world");

        ChapterDTO chapterDTO = new ChapterDTO();

        chapterDTO.setName(name);
        chapterDTO.setMarinesCount(marinesCount);
        chapterDTO.setWorld(world);
        chapterDTO.setToken(token);

        Long chapter_id = chapterService.add(chapterDTO).getId();

        historyService.addChapterHistory(chapter_id, username);

        return chapter_id;
    }

    private long saveMarine(Map<String, Object> marine, String username, TokenDTO token) throws DoesNotExistException {
        @SuppressWarnings("unchecked")
        Map<String, Object> coordinates = (Map<String, Object>) marine.get("coordinates");
        final int x = (Integer) coordinates.get("x");
        final Integer y = (Integer) coordinates.get("y");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> chapter = (Map<String, Object>) marine.get("chapter");
        long chapterId = saveChapter(chapter, username, token);

        final String name = (String) marine.get("name");
        final double health = (double) marine.get("health");
        final String achievements = (String) marine.get("achievements");
        final AstartesCategory category = AstartesCategory.valueOf((String) marine.get("category"));
        final Weapon weaponType = Weapon.valueOf((String) marine.get("weaponType"));

        SpaceMarineDTO marineDTO = new SpaceMarineDTO();
        marineDTO.setName(name);
        marineDTO.setX(x);
        marineDTO.setY(y);
        marineDTO.setHealth(health);
        marineDTO.setAchievements(achievements);
        marineDTO.setCategory(category);
        marineDTO.setWeaponType(weaponType);
        marineDTO.setToken(token);
        marineDTO.setChapterId(chapterId);

        long marine_id = spaceMarineService.add(marineDTO).getId();
        historyService.addSpaceMarineHistory(marine_id, username);

        return marine_id;
    }

    public Import addImport(Import impt) {

        log.info("Creating Import db table row" + impt.toString());

        importRepository.save(impt);

        return impt;
    }

    public Import addImport(ImportStatus status, String userName, int count, String fileName) {
        Import importObj = Import.builder()
                .status(status)
                .userName(userName)
                .count(count)
                .time(LocalDateTime.now())
                .objectName(fileName)
                .build();

        log.info("Creating Import db table row" + importObj.toString());

        importRepository.save(importObj);

        return importObj;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public ImportDTO processYamlFile(MultipartFile file, TokenDTO token)
            throws IOException, DoesNotExistException, InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, MinioException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        Queue<Pair<Class<?>, Object>> queue = new LinkedList<>();

        Yaml yaml = new Yaml();

        final long userId = jwtUtils.getIdFromToken(token.getToken());
        final User owner = userRepository.getReferenceById(userId);
        String username = owner.getName();

        int count = 0;
        try (InputStream inputStream = file.getInputStream()) {
            Map<String, List<Map<String, Object>>> parsedFile = yaml.load(inputStream);

            Boolean foundAny = false;

            List<Map<String, Object>> chapterList = parsedFile.get("chapters");
            if (chapterList != null && !chapterList.isEmpty()) {
                log.info(chapterList.toString());
                foundAny = true;
                for (Map<String, Object> chapter : chapterList) {
                    Map<String, String> errors = validateChapter(chapter);
                    if (!errors.isEmpty()) {
                        Import importObj = addImport(ImportStatus.FAILED, username, count, file.getOriginalFilename());
                        // String objectName = createImportResponse(Chapter.class, importObj).getObjectName();
                        // minioWorker.uploadLogFile(objectName);
                        return importObj.getCreatedImport();
                    }
                    count++;

                    queue.add(new Pair<Class<?>, Object>(Chapter.class, chapter));
                }
            }

            List<Map<String, Object>> marinesList = parsedFile.get("marines");
            if (marinesList != null && !marinesList.isEmpty()) {
                log.info(marinesList.toString());
                foundAny = true;
                for (Map<String, Object> marine : marinesList) {
                    Map<String, String> errors = validateMarine(marine);
                    if (!errors.isEmpty()) {
                        Import importObj = addImport(ImportStatus.FAILED, username, count, file.getOriginalFilename());
                        // String objectName = createImportResponse(SpaceMarine.class, importObj).getObjectName();
                        // minioWorker.uploadLogFile(objectName);
                        return importObj.getCreatedImport();
                    }
                    count += 1;

                    queue.add(new Pair<Class<?>, Object>(SpaceMarine.class, marine));
                }
            }
            if (!foundAny) {
                Map<String, String> errors = new HashMap<>();
                errors.put("Not found", "Not found nor chapters, nor marines.");
                
                return new ImportDTO(ImportStatus.NOT_FOUND, username, count, "", LocalDateTime.now().toString(), errors);
            }
        } catch (IOException e) {
            throw new IOException("Failed to process YAML file", e);
        }  
         
        Import impt = minioService.putObject(file, "", username, count);     
            
        while (!queue.isEmpty()) {
            Pair<Class<?>, Object> p = queue.poll();
            
            if (p.getKey() == Chapter.class) {
                saveChapter((Map<String, Object>) p.getValue(), username, token);
            } else if (p.getKey() == SpaceMarine.class) {
                saveMarine((Map<String, Object>) p.getValue(), username, token);
            }
        }

        return addImport(impt).getCreatedImport();
    }

    Map<String, String> validateChapter(Map<String, Object> chapter) {
        Map<String, String> errors = new HashMap<>();
        if (!chapter.containsKey("name") || chapter.get("name") == null || ((String) chapter.get("name")).isEmpty()) {
            errors.put("chapter.name", "Name cannot be null or empty.");
        }
        if (!chapter.containsKey("marinesCount") || !(chapter.get("marinesCount") instanceof Integer)) {
            errors.put("chapter.marinesCount", "Marines count should be integer.");
        }
        if (!chapter.containsKey("world") || chapter.get("world") == null || ((String) chapter.get("world")).isEmpty()) {
            errors.put("chapter.world", "World cannot be null or empty.");
        }

        return errors;
    }

    Map<String, String> validateCoordinates(Map<String, Object> coordinates) {
        Map<String, String> errors = new HashMap<>();
        if (!coordinates.containsKey("x") || !(coordinates.get("x") instanceof Integer)) {
            errors.put("coordinates.x", "Coordinates x should be integer.");
        }
        if (!coordinates.containsKey("y") || !(coordinates.get("y") instanceof Integer)) {
            errors.put("coordinates.y", "Coordinates y should be integer.");
        }

        return errors;
    }

    Map<String, String> validateMarine(Map<String, Object> marine) {
        Map<String, String> errors = new HashMap<>();

        if (!marine.containsKey("name") || marine.get("name") == null || ((String) marine.get("name")).isEmpty()) {
            errors.put("name", "Name cannot be null or empty.");
        }

        if (!marine.containsKey("coordinates") || !(marine.get("coordinates") instanceof Map)) {
            errors.put("coordinates", "Coordinates must be provided and must be a valid object.");
        } else {
            @SuppressWarnings("unchecked")
            Map<String, Object> coordinates = (Map<String, Object>) marine.get("coordinates");
            errors.putAll(validateCoordinates(coordinates));
        }

        if (!marine.containsKey("health") || !(marine.get("health") instanceof Number)
                || ((Number) marine.get("health")).doubleValue() <= 0) {
            errors.put("health", "Health must be greater than 0.");
        }

        if (!marine.containsKey("achievements") || marine.get("achievements") == null || ((String) marine.get("achievements")).isEmpty()) {
            errors.put("achievements", "Achievements cannot be null or empty.");
        }

        if (!marine.containsKey("category") || !(marine.get("category") instanceof String)
                || !isValidEnumValue((String) marine.get("category"), AstartesCategory.class)) {
            errors.put("category", "Category should be one of the AstartesCategory enum values.");
        }

        if (!marine.containsKey("weaponType") || !(marine.get("weaponType") instanceof String)
                || !isValidEnumValue((String) marine.get("weaponType"), Weapon.class)) {
            errors.put("weaponType", "Weapon Type should be one of the Weapon enum values.");
        }

        if (!marine.containsKey("chapter") || !(marine.get("chapter") instanceof Map)) {
            errors.put("chapter", "Chapter must be provided and must be a valid object.");
        } else {
            @SuppressWarnings("unchecked")
            Map<String, Object> chapter = (Map<String, Object>) marine.get("chapter");
            errors.putAll(validateChapter(chapter));
        }

        return errors;
    }

    private <E extends Enum<E>> boolean isValidEnumValue(String value, Class<E> enumClass) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}