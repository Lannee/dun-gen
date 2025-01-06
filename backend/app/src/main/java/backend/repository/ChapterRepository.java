package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import backend.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAll();
    void deleteById(int id);
    Optional<Chapter> getChapterById(Long id);
}
