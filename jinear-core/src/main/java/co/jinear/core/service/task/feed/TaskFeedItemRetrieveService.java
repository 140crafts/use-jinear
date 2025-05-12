package co.jinear.core.service.task.feed;

import co.jinear.core.converter.task.TaskFeedItemToDtoConverter;
import co.jinear.core.model.dto.task.TaskFeedItemDto;
import co.jinear.core.repository.task.TaskFeedItemRepository;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFeedItemRetrieveService {

    private final TaskFeedItemRepository taskFeedItemRepository;
    private final TaskFeedItemToDtoConverter taskFeedItemToDtoConverter;

    public List<TaskFeedItemDto> retrieveTaskFeedItems(String taskId, List<String> feedIds) {
        log.info("Retrieve task feed items has started. taskId: {}, feedIds: {}", taskId, NormalizeHelper.listToString(feedIds));
        return taskFeedItemRepository.findAllByTaskIdAndFeedIdIsInAndPassiveIdIsNull(taskId, feedIds)
                .stream()
                .map(taskFeedItemToDtoConverter::convert)
                .toList();
    }

    public Long retrieveTaskFeedItemCount(String taskId) {
        return taskFeedItemRepository.countAllByTaskIdAndPassiveIdIsNull(taskId);
    }
}
