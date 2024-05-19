package co.jinear.core.manager.messaging;

import co.jinear.core.converter.messaging.conversation.InitializeConversationRequestConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.request.messaging.conversation.InitializeConversationRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ConversationInitializeResponse;
import co.jinear.core.model.response.messaging.ConversationParticipantListingResponse;
import co.jinear.core.model.vo.messaging.conversation.InitializeConversationVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.messaging.conversation.ConversationOperationService;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantListingService;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantOperationService;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final ConversationParticipantRetrieveService conversationParticipantRetrieveService;
    private final ConversationParticipantOperationService conversationParticipantOperationService;

    public ConversationInitializeResponse initialize(InitializeConversationRequest initializeConversationRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAllParticipantsHasAccessIncludingOwner(initializeConversationRequest, currentAccountId);
        log.info("Initialize conversation has started. currentAccountId: {}", currentAccountId);
        InitializeConversationVo initializeConversationVo = initializeConversationRequestConverter.convert(currentAccountId, initializeConversationRequest);
        String conversationId = conversationOperationService.initializeAndRetrieveConversationId(initializeConversationVo);
        return new ConversationInitializeResponse(conversationId);
    }

    public ConversationParticipantListingResponse retrieveParticipated(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve participated conversations has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        List<ConversationParticipantDto> conversationParticipantDtos = conversationParticipantListingService.retrieveParticipations(workspaceId, currentAccountId);
        return mapResponse(conversationParticipantDtos);
    }

    public BaseResponse mute(String conversationId, ZonedDateTime silentUntil) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Mute conversation has started. conversationId: {}, currentAccountId: {}, silentUntil: {}", conversationId, currentAccountId, silentUntil);
        ConversationParticipantDto conversationParticipantDto = conversationParticipantRetrieveService.retrieve(currentAccountId, conversationId);
        conversationParticipantOperationService.updateSilentUntil(conversationParticipantDto.getConversationParticipantId(), silentUntil);
        return new BaseResponse();
    }

    public BaseResponse updateConversationParticipantLastCheck(String conversationId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        conversationParticipantRetrieveService.validateParticipantIsInConversation(currentAccountId, conversationId);
        log.info("Update conversation participant last check has started. currentAccountId: {}, conversationId: {}", currentAccountId, conversationId);
        conversationParticipantOperationService.updateLastCheck(conversationId, currentAccountId, ZonedDateTime.now());
        return new BaseResponse();
    }

    private void validateAllParticipantsHasAccessIncludingOwner(InitializeConversationRequest initializeConversationRequest, String currentAccountId) {
        Set<String> participants = new HashSet<>(initializeConversationRequest.getParticipantAccountIds());
        participants.add(currentAccountId);
        workspaceMemberService.validateAllHasAccess(initializeConversationRequest.getWorkspaceId(), participants);
    }

    private ConversationParticipantListingResponse mapResponse(List<ConversationParticipantDto> conversationParticipantDtos) {
        ConversationParticipantListingResponse conversationParticipantListingResponse = new ConversationParticipantListingResponse();
        conversationParticipantListingResponse.setParticipations(conversationParticipantDtos);
        return conversationParticipantListingResponse;
    }
}
