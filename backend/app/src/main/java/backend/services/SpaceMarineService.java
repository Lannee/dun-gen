package backend.services;

import org.springframework.stereotype.Service;

import backend.DTO.SpaceMarineDTO;
import backend.DTO.SpaceMarineEditDTO;
import backend.DTO.SpaceMarineExistedDTO;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.ObjectNotFoundException;
import backend.model.Coordinates;
import backend.model.Chapter;
import backend.model.SpaceMarine;
import backend.model.User;
import backend.repository.ChapterRepository;
import backend.repository.SpaceMarineRepository;
import backend.repository.UserRepository;
import backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpaceMarineService {
    private final SpaceMarineRepository spaceMarineRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ChapterRepository chapterRepository;

    public List<SpaceMarine> findByChapterId(long id) {
        return spaceMarineRepository.findByChapterId(id);
    }

    public boolean spaceMarinesWithChapterIdExists(long id) {
        return spaceMarineRepository.existsByChapterId(id);
    }

    public List<SpaceMarine> getSpaceMarineWithAchievementsSubstr(String substr) {
        return spaceMarineRepository.findAll().stream()
            .filter(e -> e.getAchievements().startsWith(substr))
            .collect(Collectors.toList());
    }

    public Optional<SpaceMarineExistedDTO> getMaxIdSpaceMarine(long user_id) {
        Optional<SpaceMarine> max_sm = Optional.empty();
        for (final var sm : spaceMarineRepository.findAll()) {
            if(max_sm.isEmpty() || max_sm.get().getId() < sm.getId()) {
                max_sm = Optional.of(sm);
            } 
        }

        return max_sm.map(sm -> {
                    var smDTO = sm.toDTO(); 
                    smDTO.setEditable(smDTO.getUser_id() == user_id); 
                    return smDTO;
                });
    }

    public Optional<SpaceMarine> getMaxCoordinatesSpaceMarine() {
        Optional<SpaceMarine> max_sm = Optional.empty();
        for (final var sm : spaceMarineRepository.findAll()) {
            if(max_sm.isEmpty() || (max_sm.get().getCoordinates().compareTo(sm.getCoordinates())) == -1) {
                max_sm = Optional.of(sm);
            } 
        }
        return max_sm;
    }

    public List<SpaceMarineExistedDTO> getAll(long user_id) {
        return spaceMarineRepository.findAll()
            .stream()
            .map((sm) -> {var smDTO = sm.toDTO(); smDTO.setEditable(smDTO.getUser_id() == user_id); return smDTO;})
            .collect(Collectors.toList());
    }

    public SpaceMarine add(SpaceMarineDTO req) throws DoesNotExistException {
        final long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        final User owner = userRepository.getReferenceById(userId);

        final Chapter chapter = chapterRepository.getReferenceById(req.getChapterId());
        final Coordinates coordinates = new Coordinates(req.getX(), req.getY());

        java.time.ZoneId zid = java.time.ZoneId.of("Europe/Moscow");

        SpaceMarine spaceMarine = SpaceMarine
                .builder()
                .name(req.getName())
                .coordinates(coordinates)
                .creationDate(ZonedDateTime.now(zid))
                .chapter(chapter)
                .health(req.getHealth())
                .achievements(req.getAchievements())
                .category(req.getCategory())
                .weaponType(req.getWeaponType())
                .user(owner)
                .build();


        spaceMarineRepository.save(spaceMarine);
        return spaceMarine;
    }

    public boolean delete(long personId) {
        spaceMarineRepository.deleteById(personId);
        return true;
    }

    public Optional<SpaceMarine> getById(Long id) {
        return spaceMarineRepository.getSpaceMarineById(id);
    }

    public SpaceMarine edit(SpaceMarineEditDTO req) throws ObjectNotFoundException {
        SpaceMarine spaceMarine = spaceMarineRepository.getReferenceById(req.getId());
        final Chapter chapter = chapterRepository.getReferenceById(req.getChapterId());

        spaceMarine.setName(req.getName());
        spaceMarine.setCoordinates(new Coordinates(req.getX(), req.getY()));
        spaceMarine.setChapter(chapter);
        spaceMarine.setHealth(req.getHealth());
        spaceMarine.setAchievements(req.getAchievements());
        spaceMarine.setCategory(req.getCategory());
        spaceMarine.setWeaponType(req.getWeaponType());

        spaceMarineRepository.save(spaceMarine);

        return spaceMarine;
    }
}
