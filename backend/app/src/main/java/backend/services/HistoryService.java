package backend.services;

import org.springframework.stereotype.Service;

import backend.DTO.HistoryCreatedDTO;
import backend.model.Chapter;
import backend.model.History;
import backend.model.SpaceMarine;
import backend.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public List<HistoryCreatedDTO> getChapterHistory(long id) {
        return historyRepository.findAllByObjectClassAndObjectId(Chapter.class.getName(), id);
    }
    
    public List<HistoryCreatedDTO> getSpaceMarineHistory(long id) {
        return historyRepository.findAllByObjectClassAndObjectId(SpaceMarine.class.getName(), id);
    }

    public void addChapterHistory(long object_id, String userName) {
        History history = new History(object_id, userName, Chapter.class.getName(), LocalDateTime.now());
        
        historyRepository.save(history);
    }

    public void addSpaceMarineHistory(long object_id, String userName) {
        History history = new History(object_id, userName, SpaceMarine.class.getName(), LocalDateTime.now());
        
        historyRepository.save(history);
    }

    public void deleteChapterById(long id) {
        historyRepository.deleteByObjectClassAndObjectId(Chapter.class.getName(), id);
    }

    public void deleteSpaceMarineById(long id) {
        historyRepository.deleteByObjectClassAndObjectId(SpaceMarine.class.getName(), id);
    }
}
