package co.jinear.core.service.task.list.entry;

import co.jinear.core.converter.task.TaskListEntryDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskListEntryDto;
import co.jinear.core.model.entity.task.TaskListEntry;
import co.jinear.core.repository.TaskListEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListEntryRetrieveService {

    private final TaskListEntryRepository taskListEntryRepository;
    private final TaskListEntryDtoConverter taskListEntryDtoConverter;

    public TaskListEntry retrieveEntity(String taskListEntryId) {
        log.info("Retrieve task list entry entity has started. taskListEntryId: {}", taskListEntryId);
        return taskListEntryRepository.findByTaskListEntryIdAndPassiveIdIsNull(taskListEntryId)
                .orElseThrow(NotFoundException::new);
    }

    public TaskListEntryDto retrieve(String taskListEntryId) {
        log.info("Retrieve task list entry has started. taskListEntryId: {}", taskListEntryId);
        return taskListEntryRepository.findByTaskListEntryIdAndPassiveIdIsNull(taskListEntryId)
                .map(taskListEntryDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public void validateNotExists(String taskId, String taskListId) {
        taskListEntryRepository.findByTaskIdAndTaskListIdAndPassiveIdIsNull(taskId, taskListId)
                .ifPresent(taskListEntry -> {
                    log.info("Task list entry is exist. taskListEntryId: {}", taskListEntry.getTaskListId());
                    throw new BusinessException("common.error.already-exists");
                });
    }
}
