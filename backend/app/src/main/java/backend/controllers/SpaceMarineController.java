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

import backend.model.SpaceMarine;
import backend.DTO.SpaceMarineDTO;
import backend.DTO.SpaceMarineEditDTO;
import backend.DTO.StringDTO;
import backend.DTO.HistoryCreatedDTO;
import backend.DTO.IdDTO;
import backend.DTO.TokenDTO;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.ForbiddenException;
import backend.exceptions.ObjectNotFoundException;
import backend.model.validators.TokenValidator;
import backend.security.JwtUtils;
import backend.services.AdminService;
import backend.services.HistoryService;
import backend.services.SpaceMarineService;
import backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/space_marine", produces = { "application/json" })
public class SpaceMarineController {

    private final JwtUtils jwtUtils;
    private final SpaceMarineService spaceMarineService;
    private final AdminService adminService;
    private final HistoryService historyService;
    private final UserService userService;

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id, @RequestBody TokenDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(spaceMarineService.getById(id));
        });
    }

    @PostMapping(path = "/max_id")
    public ResponseEntity<?> getMaxIdSpaceMarine(@RequestBody TokenDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());
        long userId = jwtUtils.getIdFromToken(req.getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(spaceMarineService.getMaxIdSpaceMarine(userId));
        });
    }

    @PostMapping(path = "/max_coordinates")
    public ResponseEntity<?> getMaxCoordinatesSpaceMarine(@RequestBody TokenDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(spaceMarineService.getMaxCoordinatesSpaceMarine());
        });
    }

    @PostMapping(path = "/substr")
    public ResponseEntity<?> getSpaceMarineWithAchievementsSubstr(@RequestBody StringDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(spaceMarineService.getSpaceMarineWithAchievementsSubstr(req.getString()));
        });
    }

    @PostMapping(path = "/all")
    public ResponseEntity<?> getAll(@RequestBody TokenDTO req) {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken());
        long userId = jwtUtils.getIdFromToken(req.getToken());

        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(spaceMarineService.getAll(userId));
        });
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addSpaceMarine(@RequestBody SpaceMarineDTO req) throws NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req);

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        String username = userService.getById(userId).getName();

        return ControllerExecutor.execute(validator, () -> {
            SpaceMarine spaceMarine = spaceMarineService.add(req);
            historyService.addSpaceMarineHistory(spaceMarine.getId(), username);

            return ResponseEntity.ok().body(spaceMarine);
        });
    }

    @Transactional
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteSpaceMarine(@RequestBody IdDTO req) throws ForbiddenException, ObjectNotFoundException, DoesNotExistException, NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        long spaceMarineId = req.getId();

        Optional<SpaceMarine> spaceMarine = spaceMarineService.getById(spaceMarineId);
        if(spaceMarine.isEmpty()) throw new ObjectNotFoundException("Marine with id " + spaceMarineId + " was not found!");

        if (!(adminService.isAdmin(userId)) && spaceMarine.get().getUser().getId() != userId) {
            throw new ForbiddenException("It's forbidden to you to delete this object.");
        }
        String username = userService.getById(userId).getName();

        return ControllerExecutor.execute(validator, () -> {
            spaceMarineService.delete(spaceMarineId);
            historyService.addSpaceMarineHistory(spaceMarineId, username);

            return ResponseEntity.ok().body(spaceMarine.get());
        });
    }

    @PostMapping(path = "/edit")
    public ResponseEntity<?> editPerson(@RequestBody SpaceMarineEditDTO req) throws ForbiddenException, ObjectNotFoundException, DoesNotExistException, NotFoundException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        long spaceMarineId = req.getId();

        Optional<SpaceMarine> spaceMarine = spaceMarineService.getById(spaceMarineId);

        return ControllerExecutor.execute(validator, () -> {
            if (!(adminService.isAdmin(userId)) && spaceMarine.get().getUser().getId() != userId) {
                throw new ForbiddenException("It's forbidden to you to edit this object.");
            }
            String username = userService.getById(userId).getName();

            SpaceMarine editedSpaceMarine = spaceMarineService.edit(req);
            historyService.addSpaceMarineHistory(spaceMarineId, username);
            return ResponseEntity.ok().body(editedSpaceMarine);
        });
    }

    @PostMapping(path = "/history")
    public ResponseEntity<?> getHistory(@RequestBody IdDTO req) throws ForbiddenException, ObjectNotFoundException, DoesNotExistException {
        TokenValidator validator = new TokenValidator(jwtUtils).validateToken(req.getToken().getToken());

        return ControllerExecutor.execute(validator, () -> {
            List<HistoryCreatedDTO> res = historyService.getSpaceMarineHistory(req.getId());
            return ResponseEntity.ok().body(res);
        });
    }
}
