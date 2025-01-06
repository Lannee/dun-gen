package backend.repository;

import backend.model.SpaceMarine;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceMarineRepository extends JpaRepository<SpaceMarine, Long> {
    List<SpaceMarine> findAll();
    Optional<SpaceMarine> getSpaceMarineById(Long id);
    boolean existsByChapterId(long id);
    List<SpaceMarine> findByChapterId(long id);
}
