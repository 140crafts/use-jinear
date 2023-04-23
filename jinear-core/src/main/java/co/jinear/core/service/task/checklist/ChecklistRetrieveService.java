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

    public ChecklistDto retrieveByTaskId(String taskId) {
        log.info("Retrieve checklist by task id has started. taskId: {}", taskId);
        return checklistRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .map(checklistDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Checklist retrieveEntityByTaskId(String taskId) {
        log.info("Retrieve checklist entity by task id has started. taskId: {}", taskId);
        return checklistRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean hasChecklist(String taskId) {
        log.info("Has checklist control has started. taskId: {}", taskId);
        return checklistRepository.countAllByTaskIdAndPassiveIdIsNull(taskId) > 0L;
    }
}
