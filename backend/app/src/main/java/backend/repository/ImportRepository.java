package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.model.Import;

public interface ImportRepository extends JpaRepository<Import, Long> {
    // List<HistoryCreatedDTO> findAllByObjectClassAndId(String objectClass, int id);
}