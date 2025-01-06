package backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import backend.DTO.ChapterDTO;
import backend.DTO.ChapterEditDTO;
import backend.DTO.ChapterExistedDTO;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.ObjectNotFoundException;
import backend.model.Chapter;
import backend.model.User;
import backend.repository.ChapterRepository;
import backend.repository.UserRepository;
import backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterService {
    
    private final ChapterRepository chapterRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public List<ChapterExistedDTO> getAll(long user_id) {
        return chapterRepository.findAll()
            .stream()
            .map((c) -> {var cDTO = c.toDTO(); cDTO.setEditable(cDTO.getUser_id() == user_id); return cDTO;})
            .collect(Collectors.toList());
    }

    public Chapter add(ChapterDTO req) throws DoesNotExistException {
        final long userId = jwtUtils.getIdFromToken(req.getToken().getToken());
        final User owner = userRepository.getReferenceById(userId);

        Chapter chapter = Chapter
                .builder()
                .name(req.getName())
                .marinesCount(req.getMarinesCount())
                .world(req.getWorld())
                .user(owner)
                .build();

        chapterRepository.save(chapter);
        return chapter;
    }

    public boolean delete(long id) {
        chapterRepository.deleteById(id);
        return true;
    }

    public Optional<Chapter> getById(long id) {
        return chapterRepository.getChapterById(id);
    }

    public Chapter edit(ChapterEditDTO req) throws ObjectNotFoundException {
        Chapter chapter = chapterRepository.getReferenceById(req.getId());
        chapter.setName(req.getName());
        chapter.setMarinesCount(req.getMarinesCount());
        chapter.setWorld(req.getWorld());
        chapterRepository.save(chapter);

        return chapter;
    }
}
