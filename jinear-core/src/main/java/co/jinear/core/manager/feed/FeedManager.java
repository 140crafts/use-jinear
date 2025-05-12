package co.jinear.core.manager.feed;

import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.dto.integration.FeedContentDto;
import co.jinear.core.model.dto.integration.FeedContentItemDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.FeedContentItemResponse;
import co.jinear.core.model.response.team.FeedContentResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.feed.FeedMemberOperationService;
import co.jinear.core.service.feed.FeedOperationService;
import co.jinear.core.service.feed.FeedRetrieveService;
import co.jinear.core.service.integration.feed.IntegrationFeedRetrieveStrategy;
import co.jinear.core.service.integration.feed.IntegrationFeedRetrieveStrategyFactory;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.feed.TaskFeedItemOperationService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final IntegrationFeedRetrieveStrategyFactory integrationFeedRetrieveStrategyFactory;
    private final FeedRetrieveService feedRetrieveService;
    private final FeedOperationService feedOperationService;
    private final PassiveService passiveService;
    private final TaskFeedItemOperationService taskFeedItemOperationService;
    private final FeedMemberOperationService feedMemberOperationService;

    public FeedContentResponse retrieveFeed(String workspaceId, String feedId, String pageToken) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        //TODO: cgds-302 add feed member validation
        log.info("Retrieve feed has started. workspaceId: {}, feedId: {}, currentAccountId: {}, pageToken: {}", workspaceId, feedId, currentAccountId, pageToken);
        FeedDto feedDto = feedRetrieveService.retrieveFeed(feedId);
        FeedContentDto feedContentDto = retrieveFeed(pageToken, feedDto);
        return mapResponse(feedContentDto);
    }

    public FeedContentItemResponse retrieveFeedItem(String workspaceId, String feedId, String itemId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve feed item has started. workspaceId: {}, feedId: {}, currentAccountId: {}, itemId: {}", workspaceId, feedId, currentAccountId, itemId);
        //TODO: cgds-302 add feed member validation
        FeedDto feedDto = feedRetrieveService.retrieveFeed(feedId);
        FeedContentItemDto feedContentItemDto = retrieveFeedItem(itemId, feedDto);
        return mapResponse(feedContentItemDto);
    }

    @Transactional
    public BaseResponse deleteFeed(String feedId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        FeedDto feedDto = feedRetrieveService.retrieveFeed(feedId);
        workspaceValidator.validateHasAccess(currentAccountId, feedDto.getWorkspaceId());
        //TODO: cgds-302 add feed member validation
        log.info("Delete feed has started. feedId: {}, currentAccountId: {}", feedId, currentAccountId);
        String passiveId = feedOperationService.passivizeFeed(feedId);
        taskFeedItemOperationService.updateTaskFeedItemsAsPassive(feedId, passiveId);
        feedMemberOperationService.removeAllMembers(feedId, passiveId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    private FeedContentDto retrieveFeed(String pageToken, FeedDto feedDto) {
        IntegrationFeedRetrieveStrategy integrationFeedRetrieveStrategy = integrationFeedRetrieveStrategyFactory.getStrategy(feedDto.getProvider());
        return integrationFeedRetrieveStrategy.retrieveFeed(feedDto, pageToken);
    }

    private FeedContentItemDto retrieveFeedItem(String itemId, FeedDto feedDto) {
        IntegrationFeedRetrieveStrategy integrationFeedRetrieveStrategy = integrationFeedRetrieveStrategyFactory.getStrategy(feedDto.getProvider());
        return integrationFeedRetrieveStrategy.retrieveFeedItem(feedDto, itemId);
    }

    private FeedContentResponse mapResponse(FeedContentDto feedDto) {
        FeedContentResponse feedContentResponse = new FeedContentResponse();
        feedContentResponse.setContent(feedDto);
        return feedContentResponse;
    }

    private FeedContentItemResponse mapResponse(FeedContentItemDto feedContentItemDto) {
        FeedContentItemResponse feedContentItemResponse = new FeedContentItemResponse();
        feedContentItemResponse.setFeedContentItemDto(feedContentItemDto);
        return feedContentItemResponse;
    }
}
