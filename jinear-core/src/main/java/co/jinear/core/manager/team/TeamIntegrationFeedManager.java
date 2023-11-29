package co.jinear.core.manager.team;

import co.jinear.core.model.dto.integration.FeedItemDto;
import co.jinear.core.model.dto.integration.IntegrationFeedDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.response.team.TeamIntegrationFeedItemResponse;
import co.jinear.core.model.response.team.TeamIntegrationFeedResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.integration.feed.IntegrationFeedRetrieveStrategy;
import co.jinear.core.service.integration.feed.IntegrationFeedRetrieveStrategyFactory;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamIntegrationFeedManager {

    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;
    private final TeamRetrieveService teamRetrieveService;
    private final IntegrationFeedRetrieveStrategyFactory integrationFeedRetrieveStrategyFactory;

    public TeamIntegrationFeedResponse retrieveFeed(String teamId, String pageToken) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve feed has started. teamId: {}, currentAccountId: {}, pageToken: {}", teamId, currentAccountId, pageToken);
        IntegrationInfoDto integrationInfoDto = teamRetrieveService.retrieveTeamIntegrationInfo(teamId);
        IntegrationFeedDto feedDto = retrieveFeed(pageToken, integrationInfoDto);
        return mapResponse(feedDto);
    }

    public TeamIntegrationFeedItemResponse retrieveFeedItem(String teamId, String itemId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve feed item has started. teamId: {}, currentAccountId: {}, itemId: {}", teamId, currentAccountId, itemId);
        IntegrationInfoDto integrationInfoDto = teamRetrieveService.retrieveTeamIntegrationInfo(teamId);
        FeedItemDto feedItemDto = retrieveFeedItem(itemId, integrationInfoDto);
        return mapResponse(feedItemDto);
    }

    private IntegrationFeedDto retrieveFeed(String pageToken, IntegrationInfoDto integrationInfoDto) {
        IntegrationFeedRetrieveStrategy integrationFeedRetrieveStrategy = integrationFeedRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        return integrationFeedRetrieveStrategy.retrieveFeed(integrationInfoDto.getIntegrationInfoId(), pageToken);
    }

    private FeedItemDto retrieveFeedItem(String itemId, IntegrationInfoDto integrationInfoDto) {
        IntegrationFeedRetrieveStrategy integrationFeedRetrieveStrategy = integrationFeedRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        return integrationFeedRetrieveStrategy.retrieveFeedItem(integrationInfoDto.getIntegrationInfoId(), itemId);
    }

    private TeamIntegrationFeedResponse mapResponse(IntegrationFeedDto feedDto) {
        TeamIntegrationFeedResponse teamIntegrationFeedResponse = new TeamIntegrationFeedResponse();
        teamIntegrationFeedResponse.setFeed(feedDto);
        return teamIntegrationFeedResponse;
    }

    private TeamIntegrationFeedItemResponse mapResponse(FeedItemDto feedItemDto) {
        TeamIntegrationFeedItemResponse teamIntegrationFeedItemResponse = new TeamIntegrationFeedItemResponse();
        teamIntegrationFeedItemResponse.setFeedItemDto(feedItemDto);
        return teamIntegrationFeedItemResponse;
    }
}
