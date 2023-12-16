package co.jinear.core.manager.task;

import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.dto.integration.FeedContentItemDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskFeedItemDto;
import co.jinear.core.model.dto.task.TaskFeedItemListDto;
import co.jinear.core.model.response.task.TaskFeedItemResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.feed.FeedMemberRetrieveService;
import co.jinear.core.service.integration.feed.IntegrationFeedRetrieveStrategy;
import co.jinear.core.service.integration.feed.IntegrationFeedRetrieveStrategyFactory;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.feed.TaskFeedItemRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFeedItemManager {

    private final TaskFeedItemRetrieveService taskFeedItemRetrieveService;
    private final FeedMemberRetrieveService feedMemberRetrieveService;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskAccessValidator taskAccessValidator;
    private final SessionInfoService sessionInfoService;
    private final IntegrationFeedRetrieveStrategyFactory integrationFeedRetrieveStrategyFactory;

    public TaskFeedItemResponse retrieveTaskFeedItems(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Retrieve task feed items has started. currentAccountId: {}", currentAccountId);
        List<FeedContentItemDto> feedContentItemDtos = retrieveFeedContentItemDtos(taskId, currentAccountId, taskDto);
        Long totalTaskFeedItemCount = taskFeedItemRetrieveService.retrieveTaskFeedItemCount(taskId);
        return mapResponse(feedContentItemDtos, totalTaskFeedItemCount);
    }

    private List<FeedContentItemDto> retrieveFeedContentItemDtos(String taskId, String currentAccountId, TaskDto taskDto) {
        List<FeedMemberDto> feedMemberDtos = feedMemberRetrieveService.retrieveAccountFeeds(currentAccountId, taskDto.getWorkspaceId());
        List<String> feedIds = feedMemberDtos
                .stream()
                .map(FeedMemberDto::getFeedId)
                .toList();
        List<TaskFeedItemDto> taskFeedItemDtos = taskFeedItemRetrieveService.retrieveTaskFeedItems(taskId, feedIds);
        return taskFeedItemDtos.stream()
                .map(taskFeedItemDto -> retrieveFeedItem(taskFeedItemDto.getFeedItemId(), taskFeedItemDto.getFeed()))
                .toList();
    }

    private FeedContentItemDto retrieveFeedItem(String itemId, FeedDto feedDto) {
        IntegrationFeedRetrieveStrategy integrationFeedRetrieveStrategy = integrationFeedRetrieveStrategyFactory.getStrategy(feedDto.getProvider());
        return integrationFeedRetrieveStrategy.retrieveFeedItem(feedDto, itemId);
    }

    private TaskFeedItemResponse mapResponse(List<FeedContentItemDto> feedContentItemDtos, Long totalTaskFeedItemCount) {
        TaskFeedItemListDto taskFeedItemListDto = new TaskFeedItemListDto();
        taskFeedItemListDto.setFeedContentItemList(feedContentItemDtos);
        taskFeedItemListDto.setTotalItemCount(totalTaskFeedItemCount);
        TaskFeedItemResponse taskFeedItemResponse = new TaskFeedItemResponse();
        taskFeedItemResponse.setTaskFeedItemListDto(taskFeedItemListDto);
        return taskFeedItemResponse;
    }
}
