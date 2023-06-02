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
import org.springframework.stereotype.Service;

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
        try {
            taskBoardLockService.lockTaskBoardForUpdate(initializeTaskBoardEntryVo.getTaskBoardId());
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

    private void validateNotInBoardAlready(InitializeTaskBoardEntryVo initializeTaskBoardEntryVo) {
        log.info("Validate task is not already in task board has started.");
        taskBoardEntryRetrieveService.validateNotExists(initializeTaskBoardEntryVo.getTaskId(), initializeTaskBoardEntryVo.getTaskBoardId());
    }

    public String deleteEntry(String taskBoardEntryId) {
        log.info("Delete task board entry has started. taskBoardEntryId: {}", taskBoardEntryId);
        TaskBoardEntry taskBoardEntry = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId);
        String passiveId = passiveService.createUserActionPassive();
        taskBoardEntry.setPassiveId(passiveId);
        taskBoardEntryRepository.save(taskBoardEntry);
        return passiveId;
    }

    @Transactional
    public void changeOrder(String taskBoardEntryId, Integer newOrder) {
        TaskBoardEntry taskBoardEntry = taskBoardEntryRetrieveService.retrieveEntity(taskBoardEntryId);
        Integer currentOrder = taskBoardEntry.getOrder();
        log.info("Change task board entry order has started. taskBoardEntryId: {}, currentOrder: {}, newOrder: {}", taskBoardEntryId, currentOrder, newOrder);
        final String taskBoardId = taskBoardEntry.getTaskBoardId();
        int comparison = currentOrder.compareTo(newOrder);
        switch (comparison) {
            //todo fix queries
            case -1 -> taskBoardEntryRepository.updateOrderDownward(taskBoardId, currentOrder, newOrder);
            case 1 -> taskBoardEntryRepository.updateOrderUpward(taskBoardId, currentOrder, newOrder);
            default -> log.info("Order is same.");
        }
    }

    private TaskBoardEntryDto initializeEntry(InitializeTaskBoardEntryVo initializeTaskBoardEntryVo) {
        Long taskBoardSize = taskBoardEntryRepository.countAllByTaskBoardIdAndPassiveIdIsNull(initializeTaskBoardEntryVo.getTaskBoardId());
        TaskBoardEntry taskBoardEntry = initializeTaskBoardEntryVoToEntityConverter.map(initializeTaskBoardEntryVo, taskBoardSize.intValue() + 1);
        TaskBoardEntry saved = taskBoardEntryRepository.save(taskBoardEntry);
        return taskBoardEntryDtoConverter.map(saved);
    }
}
