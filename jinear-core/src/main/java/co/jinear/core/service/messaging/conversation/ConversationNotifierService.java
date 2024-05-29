package co.jinear.core.service.messaging.conversation;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.conversation.ConversationDto;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import co.jinear.core.service.client.messageapi.model.request.EmitRequest;
import co.jinear.core.service.messaging.conversation.participant.ConversationParticipantListingService;
import co.jinear.core.service.messaging.emit.EmitterService;
import co.jinear.core.service.notification.NotificationCreateService;
import co.jinear.core.service.richtext.HtmlSanitizeService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.notification.NotificationType.MESSAGING_NEW_MESSAGE_CONVERSATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationNotifierService {

    private final ConversationParticipantListingService conversationParticipantListingService;
    private final NotificationCreateService notificationCreateService;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;
    private final HtmlSanitizeService htmlSanitizeService;
    private final EmitterService emitterService;
    private final ObjectMapper objectMapper;
    private final WorkspaceRetrieveService workspaceRetrieveService;

    @Async
    public void notify(RichMessageDto richMessageDto) {
        String conversationId = richMessageDto.getConversationId();
        List<ConversationParticipantDto> conversationParticipantDtos = conversationParticipantListingService.retrieveActiveParticipants(conversationId);
        emitMessage(richMessageDto, conversationParticipantDtos);
        sendNotification(richMessageDto, conversationParticipantDtos);
    }

    private void emitMessage(RichMessageDto richMessageDto, List<ConversationParticipantDto> conversationParticipantDtos) {
        conversationParticipantDtos.stream()
                .map(ConversationParticipantDto::getAccountId)
                .map(toAccountId -> mapEmitRequest(richMessageDto, toAccountId))
                .forEach(emitterService::emitMessage);
    }

    private EmitRequest mapEmitRequest(RichMessageDto richMessageDto, String toAccountId) {
        try {
            EmitRequest emitRequest = new EmitRequest();
            emitRequest.setChannel(toAccountId);
            emitRequest.setTopic("conversation-message");
            emitRequest.setMessage(objectMapper.writeValueAsString(richMessageDto));
            return emitRequest;
        } catch (Exception e) {
            log.error("Map emit request failed.", e);
            throw new BusinessException();
        }
    }

    private void sendNotification(RichMessageDto richMessageDto, List<ConversationParticipantDto> conversationParticipantDtos) {
        String workspaceUsername = Optional.of(richMessageDto)
                .map(RichMessageDto::getConversation)
                .map(ConversationDto::getWorkspaceId)
                .map(workspaceRetrieveService::retrieveWorkspaceWithId)
                .map(WorkspaceDto::getUsername)
                .orElse(null);
        conversationParticipantDtos.stream()
                .filter(conversationParticipantDto -> filterSender(conversationParticipantDto, richMessageDto))
                .filter(this::filterMuted)
                .map(ConversationParticipantDto::getAccountId)
                .map(toAccountId -> map(richMessageDto, toAccountId, workspaceUsername))
                .forEach(notificationCreateService::create);
    }

    private Boolean filterSender(ConversationParticipantDto conversationParticipantDto, RichMessageDto richMessageDto) {
        return !conversationParticipantDto.getAccountId().equals(richMessageDto.getAccountId());
    }

    private Boolean filterMuted(ConversationParticipantDto conversationParticipantDto) {
        return Optional.of(conversationParticipantDto)
                .map(ConversationParticipantDto::getSilentUntil)
                .map(silentUntil -> silentUntil.isBefore(ZonedDateTime.now()))
                .orElse(Boolean.TRUE);
    }

    private NotificationSendVo map(RichMessageDto richMessageDto, String toAccountId, String workspaceUsername) {
        String conversationId = richMessageDto.getConversationId();
        NotificationSendVo notificationSendVo = new NotificationSendVo();

        notificationSendVo.setAccountId(toAccountId);
        notificationSendVo.setThreadId(richMessageDto.getThreadId());
        notificationSendVo.setIsSilent(retrieveIsSilent(toAccountId));
        notificationSendVo.setNotificationType(MESSAGING_NEW_MESSAGE_CONVERSATION);

        if (Objects.nonNull(workspaceUsername) && Objects.nonNull(conversationId)) {
            notificationSendVo.setLaunchUrl("https://jinear.co/" + workspaceUsername + "/conversations/" + conversationId);
        }

        Optional.of(richMessageDto)
                .map(RichMessageDto::getConversation)
                .map(ConversationDto::getWorkspaceId)
                .ifPresent(notificationSendVo::setWorkspaceId);
        Optional.of(richMessageDto)
                .map(MessageDto::getAccount)
                .map(PlainAccountProfileDto::getUsername)
                .ifPresent(notificationSendVo::setTitle);
        Optional.of(richMessageDto)
                .map(MessageDto::getRichText)
                .map(RichTextDto::getValue)
                .map(htmlSanitizeService::toPlainText)
                .ifPresent(notificationSendVo::setText);
        return notificationSendVo;
    }

    private boolean retrieveIsSilent(String accountId) {
        log.info("Retrieve is silent has started. accountId: {}", accountId);
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(accountId);
        return Boolean.FALSE.equals(accountCommunicationPermissionDto.getPushNotification());
    }
}
