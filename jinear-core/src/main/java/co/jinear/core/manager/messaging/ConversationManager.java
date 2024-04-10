package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.conversation.InitializeConversationRequestConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.request.messaging.conversation.InitializeConversationRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ConversationParticipantListingResponse;
import co.jinear.core.model.vo.messaging.conversation.InitializeConversationVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.conversation.ConversationOperationService;
import co.jinear.core.service.messaging.conversation.ConversationParticipantListingService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationManager {

    private final SessionInfoService sessionInfoService;
    private final ConversationOperationService conversationOperationService;
    private final InitializeConversationRequestConverter initializeConversationRequestConverter;
    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceValidator workspaceValidator;
    private final ConversationParticipantListingService conversationParticipantListingService;

    public BaseResponse initialize(InitializeConversationRequest initializeConversationRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAllParticipantsHasAccessIncludingOwner(initializeConversationRequest, currentAccountId);
        log.info("Initialize conversation has started. currentAccountId: {}", currentAccountId);
        InitializeConversationVo initializeConversationVo = initializeConversationRequestConverter.convert(currentAccountId, initializeConversationRequest);
        conversationOperationService.initialize(initializeConversationVo);
        return new BaseResponse();
    }

    public ConversationParticipantListingResponse retrieveParticipated(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve participated conversations has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        List<ConversationParticipantDto> conversationParticipantDtos = conversationParticipantListingService.retrieveParticipations(workspaceId, currentAccountId);
        return mapResponse(conversationParticipantDtos);
    }

    private void validateAllParticipantsHasAccessIncludingOwner(InitializeConversationRequest initializeConversationRequest, String currentAccountId) {
        List<String> participants = new ArrayList<>(initializeConversationRequest.getParticipantAccountIds());
        participants.add(currentAccountId);
        workspaceMemberService.validateAllHasAccess(initializeConversationRequest.getWorkspaceId(), participants);
    }

    private ConversationParticipantListingResponse mapResponse(List<ConversationParticipantDto> conversationParticipantDtos) {
        ConversationParticipantListingResponse conversationParticipantListingResponse = new ConversationParticipantListingResponse();
        conversationParticipantListingResponse.setParticipations(conversationParticipantDtos);
        return conversationParticipantListingResponse;
    }
}
