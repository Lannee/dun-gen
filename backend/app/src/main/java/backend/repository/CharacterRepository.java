package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.model.Character;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}