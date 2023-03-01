package co.jinear.core.service.task.subscription;

import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.repository.task.TaskSubscriptionRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSubscriptionListingService {

    private final TaskSubscriptionRepository taskSubscriptionRepository;
    private final TaskSubscriptionConverter taskSubscriptionConverter;
    private final AccountRetrieveService accountRetrieveService;

    public List<TaskSubscriptionDto> listTaskSubscribers(String taskId) {
        log.info("List all task subscribers has started. taskId: {}", taskId);
        return taskSubscriptionRepository.findAllByTaskIdAndPassiveIdIsNull(taskId)
                .stream()
                .map(taskSubscriptionConverter::map)
                .map(this::fillAccountInfo)
                .toList();
    }

    private TaskSubscriptionDto fillAccountInfo(TaskSubscriptionDto taskSubscriptionDto) {
        PlainAccountProfileDto plainAccountProfileDto = accountRetrieveService.retrievePlainAccountProfile(taskSubscriptionDto.getAccountId());
        taskSubscriptionDto.setPlainAccountProfileDto(plainAccountProfileDto);
        return taskSubscriptionDto;
    }
}
