package backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.DTO.HistoryCreatedDTO;
import backend.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<HistoryCreatedDTO> findAllByObjectClassAndObjectId(String objectClass, long id);
    void                    deleteByObjectClassAndObjectId (String objectClass, long id);
}
