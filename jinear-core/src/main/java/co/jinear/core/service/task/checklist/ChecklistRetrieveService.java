package co.jinear.core.service.task.checklist;

import co.jinear.core.converter.task.ChecklistDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.entity.task.Checklist;
import co.jinear.core.repository.task.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistRetrieveService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistDtoConverter checklistDtoConverter;

    public ChecklistDto retrieve(String checklistId) {
        log.info("Retrieve checklist has started. checklistId: {}", checklistId);
        return checklistRepository.findByChecklistIdAndPassiveIdIsNull(checklistId)
                .map(checklistDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Checklist retrieveEntity(String checklistId) {
        log.info("Retrieve checklist entity has started. checklistId: {}", checklistId);
        return checklistRepository.findByChecklistIdAndPassiveIdIsNull(checklistId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean hasChecklist(String taskId) {
        log.info("Has checklist control has started. taskId: {}", taskId);
        return checklistRepository.countAllByTaskIdAndPassiveIdIsNull(taskId) > 0L;
    }
}
