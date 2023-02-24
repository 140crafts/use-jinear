package co.jinear.core.service.task.subscription;

import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.entity.task.TaskSubscription;
import co.jinear.core.repository.task.TaskSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSubscriptionRetrieveService {

    private final TaskSubscriptionRepository taskSubscriptionRepository;
    private final TaskSubscriptionConverter taskSubscriptionConverter;

    public TaskSubscriptionDto retrieve(String taskSubscriptionId) {
        log.info("Task subscription retrieve has started. taskSubscriptionId: {}", taskSubscriptionId);
        return taskSubscriptionRepository.findByTaskSubscriptionIdAndPassiveIdIsNull(taskSubscriptionId)
                .map(taskSubscriptionConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<TaskSubscriptionDto> retrieveOptional(String accountId, String taskId) {
        log.info("Task subscription retrieve has started. accountId: {}, taskId: {}", accountId, taskId);
        return taskSubscriptionRepository.findByAccountIdAndTaskIdAndPassiveIdIsNull(accountId, taskId)
                .map(taskSubscriptionConverter::map);
    }

    public TaskSubscriptionDto retrieve(String accountId, String taskId) {
        log.info("Task subscription retrieve has started. accountId: {}, taskId: {}", accountId, taskId);
        return taskSubscriptionRepository.findByAccountIdAndTaskIdAndPassiveIdIsNull(accountId, taskId)
                .map(taskSubscriptionConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public TaskSubscription retrieveEntity(String taskSubscriptionId) {
        log.info("Task subscription retrieve has started. taskSubscriptionId: {}", taskSubscriptionId);
        return taskSubscriptionRepository.findByTaskSubscriptionIdAndPassiveIdIsNull(taskSubscriptionId)
                .orElseThrow(NotFoundException::new);
    }

    public boolean checkSubscriptionExists(String accountId, String taskId) {
        log.info("Check subscription exists has started. accountId: {}, taskId: {}", accountId, taskId);
        long count = taskSubscriptionRepository.countAllByAccountIdAndTaskIdAndPassiveIdIsNull(accountId, taskId);
        boolean result = count > 0;
        log.info("Check subscription exists finished. exists?: {}", result);
        return result;
    }
}
