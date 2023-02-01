package co.jinear.core.manager.topic;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.response.topic.TopicListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicListingManager {

    private final TopicRetrieveService topicRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;

    public TopicListingResponse retrieveTeamTopics(String teamId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve team topics has started. accountId: {}", currentAccountId);
        Page<TopicDto> topicDtoPage = topicRetrieveService.retrieveTeamTopics(teamId, page);
        return mapResponse(topicDtoPage);
    }

    public TopicListingResponse mapResponse(Page<TopicDto> topicDtoPage) {
        TopicListingResponse topicListingResponse = new TopicListingResponse();
        topicListingResponse.setTopicDtoPage(new PageDto<>(topicDtoPage));
        return topicListingResponse;
    }
}
