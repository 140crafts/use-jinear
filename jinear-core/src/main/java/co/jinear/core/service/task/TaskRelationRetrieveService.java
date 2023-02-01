package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskRelationConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.repository.TaskRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRelationRetrieveService {

    private final TaskRelationRepository taskRelationRepository;
    private final TaskRelationConverter taskRelationConverter;

    public TaskRelationDto retrieveTaskRelation(String taskRelationId) {
        log.info("Retrieve task relation has started. taskRelationId: {}", taskRelationId);
        return taskRelationRepository.findByTaskRelationIdAndPassiveIdIsNull(taskRelationId)
                .map(taskRelationConverter::map)
                .orElseThrow(NotFoundException::new);
    }
}
