package co.jinear.core.service.task.board.entry;

import co.jinear.core.converter.task.InitializeTaskBoardEntryVoToEntityConverter;
import co.jinear.core.converter.task.TaskBoardEntryDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import co.jinear.core.model.vo.task.InitializeTaskBoardEntryVo;
import co.jinear.core.repository.TaskBoardEntryRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.board.TaskBoardLockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardEntryOperationService {

    private final TaskBoardEntryRetrieveService taskBoardEntryRetrieveService;
    private final TaskBoardEntryRepository taskBoardEntryRepository;
    private final InitializeTaskBoardEntryVoToEntityConverter initializeTaskBoardEntryVoToEntityConverter;
    private final TaskBoardEntryDtoConverter taskBoardEntryDtoConverter;
    private final TaskBoardLockService taskBoardLockService;
    private final PassiveService passiveService;

    public TaskBoardEntryDto initialize(InitializeTaskBoardEntryVo initializeTaskBoardEntryVo) {
        log.info("Initialize task board entry has started. initializeTaskBoardEntryVo: {}", initializeTaskBoardEntryVo);
        taskBoardLockService.lockTaskBoardForUpdate(initializeTaskBoardEntryVo.getTaskBoardId());
        try {
            validateNotInBoardAlready(initializeTaskBoardEntryVo);
            return initializeEntry(initializeTaskBoardEntryVo);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Initialize task list entry has failed.", e);
            throw new BusinessException();
        } finally {
            taskBoardLockService.unlockTaskBoardForUpdate(initializeTaskBoardEntryVo.getTaskBoardId());
        }
    }

    @Transactional
    public TaskBoardEntryDto deleteEntry(String taskBoardEntryId) {
        log.info("Delete task board entry has started. taskBoardEntryId: {}", taskBoardEntryId);
        return withBoardLock(taskBoardEntryId, () -> moveToLastAndPassivate(taskBoardEntryId));
    }

    @Transactional
    public TaskBoardEntryDto changeOrder(String taskBoardEntryId, Integer newOrder) {
        return withBoardLock(taskBoardEntryId, () -> applyOrder(taskBoardEntryId, newOrder));
    }

    @Transactional
    public TaskBoardEntryDto changeFilteredOrder(String taskBoardEntryId, String taskBoardEntryIdBefore, String taskBoardEntryIdAfter) {
        return withBoardLock(taskBoardEntryId, () -> applyFilteredOrder(taskBoardEntryId, taskBoardEntryIdBefore, taskBoardEntryIdAfter));
    }

    private void validateNotInBoardAlready(InitializeTaskBoardEntryVo initializeTaskBoardEntryVo) {
        log.info("Validate task is not already in task board has started.");
        taskBoardEntryRetrieveService.validateNotExists(initializeTaskBoardEntryVo.getTaskId(), initializeTaskBoardEntryVo.getTaskBoardId());
    }

    private TaskBoardEntryDto withBoardLock(String taskBoardEntryId, Supplier<TaskBoardEntryDto> operation) {
        String taskBoardId = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId).getTaskBoardId();
        taskBoardLockService.lockTaskBoardForUpdate(taskBoardId);
        try {
            return operation.get();
        } finally {
            taskBoardLockService.unlockTaskBoardForUpdate(taskBoardId);
        }
    }

    private TaskBoardEntryDto moveToLastAndPassivate(String taskBoardEntryId) {
        moveToLast(taskBoardEntryId);
        TaskBoardEntry taskBoardEntry = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId);
        String passiveId = passiveService.createUserActionPassive();
        taskBoardEntry.setPassiveId(passiveId);
        TaskBoardEntry saved = taskBoardEntryRepository.save(taskBoardEntry);
        return taskBoardEntryDtoConverter.map(saved);
    }

    private TaskBoardEntryDto applyOrder(String taskBoardEntryId, Integer newOrder) {
        TaskBoardEntry taskBoardEntry = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId);
        Integer currentOrder = taskBoardEntry.getOrder();
        log.info("Change task board entry order has started. taskBoardEntryId: {}, currentOrder: {}, newOrder: {}", taskBoardEntryId, currentOrder, newOrder);
        final String taskBoardId = taskBoardEntry.getTaskBoardId();
        int comparison = currentOrder.compareTo(newOrder);
        log.info("Order comparison result: {}", comparison);
        switch (comparison) {
            case -1 -> taskBoardEntryRepository.updateOrderDownward(taskBoardId, currentOrder, newOrder);
            case 1 -> taskBoardEntryRepository.updateOrderUpward(taskBoardId, currentOrder, newOrder);
            default -> log.info("Order is same.");
        }
        taskBoardEntry.setOrder(newOrder);
        TaskBoardEntry saved = taskBoardEntryRepository.save(taskBoardEntry);
        return taskBoardEntryDtoConverter.map(saved);
    }

    private TaskBoardEntryDto applyFilteredOrder(String taskBoardEntryId, String taskBoardEntryIdBefore, String taskBoardEntryIdAfter) {
        TaskBoardEntry taskBoardEntry = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId);
        Integer currentOrder = taskBoardEntry.getOrder();
        Integer newOrder = retrieveOrder(taskBoardEntryIdBefore)
                .filter(beforeOrder -> beforeOrder > currentOrder)
                .or(() -> retrieveOrder(taskBoardEntryIdAfter).filter(afterOrder -> afterOrder < currentOrder))
                .orElse(currentOrder);
        return applyOrder(taskBoardEntryId, newOrder);
    }

    private Optional<Integer> retrieveOrder(String taskBoardEntryId) {
        return Optional.ofNullable(taskBoardEntryId)
                .filter(StringUtils::isNotBlank)
                .map(taskBoardEntryRetrieveService::retrieveEntity)
                .map(TaskBoardEntry::getOrder);
    }

    private void moveToLast(String taskBoardEntryId) {
        TaskBoardEntry taskBoardEntry = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId);
        Integer lastOrder = taskBoardEntryRetrieveService.retrieveLastOrder(taskBoardEntry.getTaskBoardId());
        applyOrder(taskBoardEntryId, lastOrder);
    }

    private TaskBoardEntryDto initializeEntry(InitializeTaskBoardEntryVo initializeTaskBoardEntryVo) {
        Integer nextOrder = taskBoardEntryRetrieveService.retrieveNextOrder(initializeTaskBoardEntryVo.getTaskBoardId());
        TaskBoardEntry taskBoardEntry = initializeTaskBoardEntryVoToEntityConverter.map(initializeTaskBoardEntryVo, nextOrder);
        TaskBoardEntry saved = taskBoardEntryRepository.save(taskBoardEntry);
        return taskBoardEntryDtoConverter.map(saved);
    }
}
