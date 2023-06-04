package co.jinear.core.service.task.board.entry;

import co.jinear.core.converter.task.TaskBoardEntryDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import co.jinear.core.repository.TaskBoardEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardEntryRetrieveService {

    private final TaskBoardEntryRepository taskBoardEntryRepository;
    private final TaskBoardEntryDtoConverter taskBoardEntryDtoConverter;

    public TaskBoardEntry retrieveEntity(String taskBoardEntryId) {
        log.info("Retrieve task board entry entity has started. taskBoardEntryId: {}", taskBoardEntryId);
        return taskBoardEntryRepository.findByTaskBoardEntryIdAndPassiveIdIsNull(taskBoardEntryId)
                .orElseThrow(NotFoundException::new);
    }

    public TaskBoardEntryDto retrieve(String taskBoardEntryId) {
        log.info("Retrieve task board entry has started. taskBoardEntryId: {}", taskBoardEntryId);
        return taskBoardEntryRepository.findByTaskBoardEntryIdAndPassiveIdIsNull(taskBoardEntryId)
                .map(taskBoardEntryDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public void validateNotExists(String taskId, String taskBoardId) {
        taskBoardEntryRepository.findByTaskIdAndTaskBoardIdAndPassiveIdIsNull(taskId, taskBoardId)
                .ifPresent(taskBoardEntry -> {
                    log.info("Task board entry is exist. taskBoardEntryId: {}", taskBoardEntry.getTaskBoardEntryId());
                    throw new BusinessException("common.error.already-exists");
                });
    }
}
