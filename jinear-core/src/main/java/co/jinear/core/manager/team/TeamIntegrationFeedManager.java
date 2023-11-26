package co.jinear.core.manager.team;

import co.jinear.core.model.dto.integration.IntegrationFeedDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
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

    public TeamIntegrationFeedResponse retrieveFeed(String teamId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve feed has started. currentAccountId: {}, page: {}", currentAccountId, page);
        IntegrationInfoDto integrationInfoDto = teamRetrieveService.retrieveTeamIntegrationInfo(teamId);
        IntegrationFeedDto feedDto = retrieveFeed(page, integrationInfoDto);
        return mapResponse(feedDto);
    }

    private IntegrationFeedDto retrieveFeed(int page, IntegrationInfoDto integrationInfoDto) {
        IntegrationFeedRetrieveStrategy integrationFeedRetrieveStrategy = integrationFeedRetrieveStrategyFactory.getStrategy(integrationInfoDto.getProvider());
        return integrationFeedRetrieveStrategy.retrieveFeed(integrationInfoDto.getIntegrationInfoId(), page);
    }

    private TeamIntegrationFeedResponse mapResponse(IntegrationFeedDto feedDto) {
        TeamIntegrationFeedResponse teamIntegrationFeedResponse = new TeamIntegrationFeedResponse();
        teamIntegrationFeedResponse.setFeed(feedDto);
        return teamIntegrationFeedResponse;
    }
}
