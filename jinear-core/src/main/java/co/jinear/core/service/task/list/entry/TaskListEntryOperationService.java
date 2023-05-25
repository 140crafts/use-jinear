package co.jinear.core.service.task.list.entry;

import co.jinear.core.converter.task.InitializeTaskListEntryVoToEntityConverter;
import co.jinear.core.converter.task.TaskListEntryDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.task.TaskListEntryDto;
import co.jinear.core.model.entity.task.TaskListEntry;
import co.jinear.core.model.vo.task.InitializeTaskListEntryVo;
import co.jinear.core.repository.TaskListEntryRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.list.TaskListLockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListEntryOperationService {

    private final TaskListEntryRetrieveService taskListEntryRetrieveService;
    private final TaskListEntryRepository taskListEntryRepository;
    private final InitializeTaskListEntryVoToEntityConverter initializeTaskListEntryVoToEntityConverter;
    private final TaskListEntryDtoConverter taskListEntryDtoConverter;
    private final TaskListLockService taskListLockService;
    private final PassiveService passiveService;

    public TaskListEntryDto initialize(InitializeTaskListEntryVo initializeTaskListEntryVo) {
        log.info("Initialize task list entry has started. initializeTaskListEntryVo: {}", initializeTaskListEntryVo);
        try {
            taskListLockService.lockTaskListForUpdate(initializeTaskListEntryVo.getTaskListId());
            return initializeEntry(initializeTaskListEntryVo);
        } catch (Exception e) {
            log.error("Initialize task list entry has failed.", e);
            throw new BusinessException();
        } finally {
            taskListLockService.unlockTaskListForUpdate(initializeTaskListEntryVo.getTaskListId());
        }
    }

    public String deleteEntry(String taskListEntryId) {
        log.info("Delete task list entry has started. taskListEntryId: {}", taskListEntryId);
        TaskListEntry taskListEntry = taskListEntryRetrieveService.retrieveEntity(taskListEntryId);
        String passiveId = passiveService.createUserActionPassive();
        taskListEntry.setPassiveId(passiveId);
        taskListEntryRepository.save(taskListEntry);
        return passiveId;
    }

    @Transactional
    public void changeOrder(String taskListEntryId, Integer newOrder) {
        TaskListEntry taskListEntry = taskListEntryRetrieveService.retrieveEntity(taskListEntryId);
        Integer currentOrder = taskListEntry.getOrder();
        log.info("Change task list entry order has started. taskListEntryId: {}, currentOrder: {}, newOrder: {}", taskListEntryId, currentOrder, newOrder);
        final String taskListId = taskListEntry.getTaskListId();
        int comparison = currentOrder.compareTo(newOrder);
        switch (comparison) {
            case -1 -> taskListEntryRepository.updateOrderDownward(taskListId, currentOrder, newOrder);
            case 1 -> taskListEntryRepository.updateOrderUpward(taskListId, currentOrder, newOrder);
            default -> log.info("Order is same.");
        }
    }

    private TaskListEntryDto initializeEntry(InitializeTaskListEntryVo initializeTaskListEntryVo) {
        Long taskListSize = taskListEntryRepository.countAllByTaskListIdAndPassiveIdIsNull(initializeTaskListEntryVo.getTaskListId());
        TaskListEntry taskListEntry = initializeTaskListEntryVoToEntityConverter.map(initializeTaskListEntryVo, taskListSize.intValue() + 1);
        TaskListEntry saved = taskListEntryRepository.save(taskListEntry);
        return taskListEntryDtoConverter.map(saved);
    }
}
