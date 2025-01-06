package backend.controllers;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import backend.DTO.HistoryCreatedDTO;
import backend.DTO.IdDTO;
import backend.DTO.ChapterDTO;
import backend.DTO.ChapterEditDTO;
import backend.DTO.TokenDTO;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.ForbiddenException;
import backend.exceptions.ObjectNotFoundException;
import backend.model.Chapter;
import backend.model.validators.TokenValidator;
import backend.security.JwtUtils;
import backend.services.AdminService;
import backend.services.ChapterService;
import backend.services.HistoryService;
import backend.services.SpaceMarineService;
import backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/chapter", produces = { "application/json" })
public class ChapterController {

    private final JwtUtils jwtUtils;
    private final ChapterService chapterService;
    private final AdminService adminService;
    private final HistoryService historyService;
    private final UserService userService;
    private final SpaceMarineService spaceMarineService;

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id, @RequestBody TokenDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(chapterService.getById(id));
        });
    }

    @PostMapping(path = "/all")
    public ResponseEntity<?> getAll(@RequestBody TokenDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());
        long userId = jwtUtils.getIdFromToken(req.getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(chapterService.getAll(userId));
        });
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addChapter(@RequestBody ChapterDTO req) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        String username = userService.getById(userId).getName();

        return ControllerExecutor.execute(validator, () -> {
            Chapter chapter = chapterService.add(req);
            historyService.addChapterHistory(chapter.getId(), username);

            return ResponseEntity.ok().body(chapter);
        });
    }

    @Transactional
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteChapter(@RequestBody IdDTO req) throws ForbiddenException, ObjectNotFoundException, DoesNotExistException, NotFoundException{
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        long chapterId = req.getId();
        Optional<Chapter> chapter = chapterService.getById(chapterId);

        return ControllerExecutor.execute(validator, () -> {
            if(chapter.isEmpty()) throw new ObjectNotFoundException("Chapter with id " + chapterId + " was not found!");

            final boolean isAdmin = adminService.isAdmin(userId);
            if (!isAdmin && chapter.get().getUser().getId() != userId) {
                throw new ForbiddenException("It's forbidden to you to delete this object.");
            }
    
            boolean existNotUserMarinesWithThisChapter = spaceMarineService.findByChapterId(chapterId).stream().anyMatch(e -> {return e.getUser().getId() != userId;});
    
            if (!isAdmin && existNotUserMarinesWithThisChapter)
                throw new ForbiddenException("It's forbidden to you to delete this chapter because of space marines you had not created");
    
            String username = userService.getById(userId).getName();
            
            spaceMarineService.findByChapterId(chapterId).forEach(e -> {
                spaceMarineService.delete(e.getId());
            });

            chapterService.delete(chapterId);
            historyService.addChapterHistory(chapterId, username);
            
            return ResponseEntity.ok().body(chapter.get());
        });
    }

    @PostMapping(path = "/edit")
    public ResponseEntity<?> editChapter(@RequestBody ChapterEditDTO req) throws ForbiddenException, ObjectNotFoundException, DoesNotExistException, NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        long chapterId = req.getId();
        Optional<Chapter> chapter = chapterService.getById(chapterId);
        if(chapter.isEmpty()) throw new ObjectNotFoundException("Chapter with id " + chapterId + " was not found!");

        return ControllerExecutor.execute(validator, () -> {
            
            if (!adminService.isAdmin(userId) && chapter.get().getUser().getId() != userId) {
                throw new ForbiddenException("It's forbidden to you to edit this object.");
            }
            String username = userService.getById(userId).getName();

            Chapter editedChapter = chapterService.edit(req);
            historyService.addChapterHistory(chapterId, username);

            return ResponseEntity.ok().body(editedChapter);
        });
    }

    @PostMapping(path = "/history")
    public ResponseEntity<?> getHistory(@RequestBody IdDTO req) throws ForbiddenException, ObjectNotFoundException, DoesNotExistException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        return ControllerExecutor.execute(validator, () -> {
            List<HistoryCreatedDTO> res = historyService.getChapterHistory(req.getId());

            return ResponseEntity.ok().body(res);
        });
    }
}
