package co.jinear.core.manager.topic;

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
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicManager {

    private final TopicInitializeService topicInitializeService;
    private final TopicRetrieveService topicRetrieveService;
    private final TopicUpdateService topicUpdateService;
    private final ModelMapper modelMapper;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;


    public TopicResponse initializeTopic(TopicInitializeRequest topicInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(topicInitializeRequest, currentAccountId);
        log.info("Initialize topic has started. currentAccountId: {}", currentAccountId);
        TopicInitializeVo topicInitializeVo = modelMapper.map(topicInitializeRequest, TopicInitializeVo.class);
        topicInitializeVo.setOwnerId(currentAccountId);
        TopicDto topicDto = topicInitializeService.initializeTopic(topicInitializeVo);
        return mapResponse(topicDto);
    }

    public TopicResponse updateTopic(TopicUpdateRequest topicUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccessWithTopicId(topicUpdateRequest.getTopicId(), currentAccountId);
        log.info("Update topic has started. currentAccountId: {}, topicUpdateRequest: {}", currentAccountId, topicUpdateRequest);
        TopicUpdateVo topicUpdateVo = modelMapper.map(topicUpdateRequest, TopicUpdateVo.class);
        TopicDto topicDto = topicUpdateService.updateTopic(topicUpdateVo);
        return mapResponse(topicDto);
    }

    public BaseResponse deleteTopic(String topicId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccessWithTopicId(topicId, currentAccountId);
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

    private void validateWorkspaceAccess(TopicInitializeRequest topicInitializeRequest, String currentAccountId) {
        workspaceValidator.validateHasAccess(currentAccountId, topicInitializeRequest.getWorkspaceId());
    }

    private void validateWorkspaceAccessWithTopicId(String topicId, String currentAccountId) {
        TopicDto topicDto = topicRetrieveService.retrieve(topicId);
        workspaceValidator.validateHasAccess(currentAccountId, topicDto.getWorkspaceId());
    }
}