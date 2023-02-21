package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.response.topic.TaskSubscriptionResponse;
import co.jinear.core.model.vo.task.TaskSubscriptionInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.subscription.TaskSubscriptionOperationService;
import co.jinear.core.service.task.subscription.TaskSubscriptionRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSubscriptionManager {

    private final SessionInfoService sessionInfoService;
    private final TaskAccessValidator taskAccessValidator;
    private final TaskSubscriptionConverter taskSubscriptionConverter;
    private final TaskSubscriptionOperationService taskSubscriptionOperationService;
    private final TaskSubscriptionRetrieveService taskSubscriptionRetrieveService;

    public TaskSubscriptionResponse retrieveTaskSubscription(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Retrieve task subscription has started from accountId: {}", currentAccountId);
        return taskSubscriptionRetrieveService.retrieveOptional(currentAccountId, taskId)
                .map(taskSubscriptionConverter::map)
                .orElseGet(TaskSubscriptionResponse::new);
    }

    public TaskSubscriptionResponse initializeTaskSubscription(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Task subscription initialize has started. currentAccountId: {}", currentAccountId);
        TaskSubscriptionInitializeVo taskSubscriptionInitializeVo = taskSubscriptionConverter.map(currentAccountId, taskId);
        TaskSubscriptionDto taskSubscriptionDto = taskSubscriptionOperationService.initializeTaskSubscription(taskSubscriptionInitializeVo)
                .orElseGet(() -> taskSubscriptionRetrieveService.retrieve(currentAccountId,taskId));
        return taskSubscriptionConverter.map(taskSubscriptionDto);
    }
}
