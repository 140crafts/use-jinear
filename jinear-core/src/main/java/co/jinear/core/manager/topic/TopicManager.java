package co.jinear.core.manager.topic;

import co.jinear.core.converter.topic.TopicInitializeVoConverter;
import co.jinear.core.converter.topic.TopicUpdateVoConverter;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.request.topic.TopicInitializeRequest;
import co.jinear.core.model.request.topic.TopicUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.topic.TopicResponse;
import co.jinear.core.model.vo.topic.TopicDeleteVo;
import co.jinear.core.model.vo.topic.TopicInitializeVo;
import co.jinear.core.model.vo.topic.TopicUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.topic.TopicInitializeService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.topic.TopicUpdateService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicManager {

    private final TopicInitializeService topicInitializeService;
    private final TopicRetrieveService topicRetrieveService;
    private final TopicUpdateService topicUpdateService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TopicInitializeVoConverter topicInitializeVoConverter;
    private final TopicUpdateVoConverter topicUpdateVoConverter;

    public TopicResponse retrieveTopic(String topicId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(topicId, currentAccountId);
        log.info("Retrieve topic has started. currentAccountId: {}", currentAccountId);
        TopicDto topicDto = topicRetrieveService.retrieve(topicId);
        return mapResponse(topicDto);
    }

    public TopicResponse retrieveTopicByTag(String teamId, String workspaceId, String topicTag) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve topic has started. currentAccountId: {}", currentAccountId);
        TopicDto topicDto = topicRetrieveService.retrieveByTag(teamId, workspaceId, topicTag);
        validateAccess(topicDto, currentAccountId);
        return mapResponse(topicDto);
    }

    public TopicResponse initializeTopic(TopicInitializeRequest topicInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(topicInitializeRequest, currentAccountId);
        log.info("Initialize topic has started. currentAccountId: {}", currentAccountId);
        TopicInitializeVo topicInitializeVo = topicInitializeVoConverter.map(topicInitializeRequest, currentAccountId);
        TopicDto topicDto = topicInitializeService.initializeTopic(topicInitializeVo);
        return mapResponse(topicDto);
    }

    public TopicResponse updateTopic(TopicUpdateRequest topicUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(topicUpdateRequest.getTopicId(), currentAccountId);
        log.info("Update topic has started. currentAccountId: {}, topicUpdateRequest: {}", currentAccountId, topicUpdateRequest);
        TopicUpdateVo topicUpdateVo = topicUpdateVoConverter.map(topicUpdateRequest);
        TopicDto topicDto = topicUpdateService.updateTopic(topicUpdateVo);
        return mapResponse(topicDto);
    }

    public BaseResponse deleteTopic(String topicId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(topicId, currentAccountId);
        log.info("Delete topic has started. currentAccountId: {}, topicId: {}", currentAccountId, topicId);
        topicUpdateService.deleteTopic(
                TopicDeleteVo.builder()
                        .topicId(topicId)
                        .responsibleAccountId(currentAccountId)
                        .build());
        return new BaseResponse();
    }

    private TopicResponse mapResponse(TopicDto topicDto) {
        TopicResponse topicResponse = new TopicResponse();
        topicResponse.setTopicDto(topicDto);
        return topicResponse;
    }

    private void validateAccess(TopicInitializeRequest topicInitializeRequest, String currentAccountId) {
        workspaceValidator.validateHasAccess(currentAccountId, topicInitializeRequest.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(currentAccountId, topicInitializeRequest.getTeamId());
    }

    private void validateAccess(String topicId, String currentAccountId) {
        TopicDto topicDto = topicRetrieveService.retrieve(topicId);
        validateAccess(topicDto, currentAccountId);
    }

    private void validateAccess(TopicDto topicDto, String currentAccountId) {
        workspaceValidator.validateHasAccess(currentAccountId, topicDto.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(currentAccountId, topicDto.getTeamId());
    }
}