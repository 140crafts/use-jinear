package co.jinear.core.service.task.subscription;

import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.entity.task.TaskSubscription;
import co.jinear.core.model.vo.task.TaskSubscriptionInitializeVo;
import co.jinear.core.repository.task.TaskSubscriptionRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSubscriptionOperationService {

    private final TaskSubscriptionRepository taskSubscriptionRepository;
    private final TaskSubscriptionConverter taskSubscriptionConverter;
    private final TaskSubscriptionRetrieveService taskSubscriptionRetrieveService;
    private final PassiveService passiveService;

    public Optional<TaskSubscriptionDto> initializeTaskSubscription(TaskSubscriptionInitializeVo taskSubscriptionInitializeVo) {
        log.info("Initialize task subscription has started. taskSubscriptionInitializeVo: {}", taskSubscriptionInitializeVo);
        if (checkAlreadySubscribed(taskSubscriptionInitializeVo)) {
            return Optional.empty();
        }
        TaskSubscription taskSubscription = taskSubscriptionConverter.map(taskSubscriptionInitializeVo);
        TaskSubscription saved = taskSubscriptionRepository.save(taskSubscription);
        log.info("Initialize task subscription has finished.");
        return Optional.of(taskSubscriptionConverter.map(saved));
    }

    public void removeTaskSubscription(String taskSubscriptionId, String performedAccountId) {
        log.info("Remove task subscription has started. taskSubscriptionId: {}, performed accountId: {}", taskSubscriptionId, performedAccountId);
        TaskSubscription taskSubscription = taskSubscriptionRetrieveService.retrieveEntity(taskSubscriptionId);
        String passiveId = passiveService.createUserActionPassive(performedAccountId);
        taskSubscription.setPassiveId(passiveId);
        taskSubscriptionRepository.save(taskSubscription);
        log.info("Remove task subscription has finished. taskSubscriptionId: {}, passiveId: {}", taskSubscriptionId, passiveId);
    }

    private boolean checkAlreadySubscribed(TaskSubscriptionInitializeVo taskSubscriptionInitializeVo) {
        return taskSubscriptionRetrieveService.checkSubscriptionExists(taskSubscriptionInitializeVo.getAccountId(), taskSubscriptionInitializeVo.getTaskId());
    }
}
