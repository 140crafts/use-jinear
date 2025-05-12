package co.jinear.core.service.task.feed;

import co.jinear.core.converter.task.InitializeTaskFeedItemVoToEntityConverter;
import co.jinear.core.model.entity.task.TaskFeedItem;
import co.jinear.core.model.vo.task.InitializeTaskFeedItemVo;
import co.jinear.core.repository.task.TaskFeedItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFeedItemOperationService {

    private final TaskFeedItemRepository taskFeedItemRepository;
    private final InitializeTaskFeedItemVoToEntityConverter initializeTaskFeedItemVoToEntityConverter;

    public void initializeTaskFeedItemRelation(InitializeTaskFeedItemVo initializeTaskFeedItemVo) {
        log.info("Initialize task feed item relation has started. initializeTaskFeedItemVo: {}", initializeTaskFeedItemVo);
        TaskFeedItem taskFeedItem = initializeTaskFeedItemVoToEntityConverter.convert(initializeTaskFeedItemVo);
        taskFeedItemRepository.save(taskFeedItem);
        log.info("Initialize task feed item relation has completed.");
    }

    @Transactional
    public void updateTaskFeedItemsAsPassive(String feedId, String passiveId) {
        log.info("Update task feed items as passive has started. feedId: {}, passiveId: {}", feedId, passiveId);
        taskFeedItemRepository.updateTaskFeedItemsAsPassive(feedId,passiveId);
    }
}
