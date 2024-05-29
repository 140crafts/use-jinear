package co.jinear.core.service.messaging.channel;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import co.jinear.core.service.client.messageapi.model.request.EmitRequest;
import co.jinear.core.service.messaging.emit.EmitterService;
import co.jinear.core.service.messaging.message.MessageRetrieveService;
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
public class ChannelNotifierService {

    private final ChannelMemberListingService channelMemberListingService;
    private final NotificationCreateService notificationCreateService;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;
    private final HtmlSanitizeService htmlSanitizeService;
    private final EmitterService emitterService;
    private final ObjectMapper objectMapper;
    private final MessageRetrieveService messageRetrieveService;
    private final WorkspaceRetrieveService workspaceRetrieveService;

    @Async
    public void notifyChannelMembers(String channelId, String messageId) {
        log.info("Notify channel members has started. channelId: {}, firstMessageId: {}", channelId, messageId);
        RichMessageDto firstMessage = messageRetrieveService.retrieveRich(messageId);
        List<ChannelMemberDto> channelMembers = channelMemberListingService.retrieveChannelMembers(channelId);
        emitMessage(firstMessage, channelMembers);
        notifyMembers(firstMessage, channelMembers);
    }

    private void emitMessage(RichMessageDto firstMessage, List<ChannelMemberDto> channelMembers) {
        channelMembers.stream()
                .map(ChannelMemberDto::getAccountId)
                .map(accountId -> mapEmitRequest(firstMessage, accountId))
                .forEach(emitterService::emitMessage);
    }

    private void notifyMembers(RichMessageDto firstMessage, List<ChannelMemberDto> channelMembers) {
        String workspaceUsername = Optional.of(firstMessage)
                .map(RichMessageDto::getThread)
                .map(ThreadDto::getChannel)
                .map(PlainChannelDto::getWorkspaceId)
                .map(workspaceRetrieveService::retrieveWorkspaceWithId)
                .map(WorkspaceDto::getUsername)
                .orElse(null);
        channelMembers.stream()
                .filter(channelMemberDto -> filterSender(firstMessage, channelMemberDto))
                .filter(this::filterMuted)
                .map(ChannelMemberDto::getAccountId)
                .map(toAccountId -> map(firstMessage, toAccountId, workspaceUsername))
                .forEach(notificationCreateService::create);
    }

    private EmitRequest mapEmitRequest(RichMessageDto richMessageDto, String toAccountId) {
        try {
            EmitRequest emitRequest = new EmitRequest();
            emitRequest.setChannel(toAccountId);
            emitRequest.setTopic("thread-message");
            emitRequest.setMessage(objectMapper.writeValueAsString(richMessageDto));
            return emitRequest;
        } catch (Exception e) {
            log.error("Map emit request failed.", e);
            throw new BusinessException();
        }
    }

    private boolean filterSender(MessageDto firstMessage, ChannelMemberDto channelMemberDto) {
        return !channelMemberDto.getAccountId().equals(firstMessage.getAccountId());
    }

    private Boolean filterMuted(ChannelMemberDto channelMemberDto) {
        return Optional.of(channelMemberDto)
                .map(ChannelMemberDto::getSilentUntil)
                .map(silentUntil -> silentUntil.isBefore(ZonedDateTime.now()))
                .orElse(Boolean.TRUE);
    }

    private NotificationSendVo map(RichMessageDto firstMessage, String toAccountId, String workspaceUsername) {
        NotificationSendVo notificationSendVo = new NotificationSendVo();
        String channelId = Optional.of(firstMessage).map(RichMessageDto::getThread).map(ThreadDto::getChannelId).orElse(null);
        String threadId = Optional.of(firstMessage).map(RichMessageDto::getThreadId).orElse(null);

        notificationSendVo.setAccountId(toAccountId);
        notificationSendVo.setThreadId(firstMessage.getThreadId());
        notificationSendVo.setIsSilent(retrieveIsSilent(toAccountId));
        notificationSendVo.setNotificationType(MESSAGING_NEW_MESSAGE_CONVERSATION);

        if (Objects.nonNull(workspaceUsername) && Objects.nonNull(channelId) && Objects.nonNull(threadId)) {
            notificationSendVo.setLaunchUrl("https://jinear.co/" + workspaceUsername + "/conversations/channel/" + channelId + "/thread/" + threadId);
        }

        Optional.of(firstMessage)
                .map(RichMessageDto::getThread)
                .map(ThreadDto::getChannel)
                .map(PlainChannelDto::getWorkspaceId)
                .ifPresent(notificationSendVo::setWorkspaceId);
        Optional.of(firstMessage)
                .map(MessageDto::getAccount)
                .map(PlainAccountProfileDto::getUsername)
                .ifPresent(notificationSendVo::setTitle);
        Optional.of(firstMessage)
                .map(MessageDto::getRichText)
                .map(RichTextDto::getValue)
                .map(htmlSanitizeService::toPlainText)
                .ifPresent(notificationSendVo::setText);
        return notificationSendVo;
    }

    private boolean retrieveIsSilent(String accountId) {
        log.info("Retrieve is silent has started. accountId: {}", accountId);
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(accountId);
        return Boolean.TRUE.equals(accountCommunicationPermissionDto.getPushNotification());
    }
}
