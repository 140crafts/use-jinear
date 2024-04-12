package co.jinear.core.service.messaging.conversation;

import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import co.jinear.core.service.notification.NotificationCreateService;
import co.jinear.core.service.richtext.HtmlSanitizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
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

    @Async
    public void notify(RichMessageDto richMessageDto) {
        String conversationId = richMessageDto.getConversationId();
        List<ConversationParticipantDto> conversationParticipantDtos = conversationParticipantListingService.retrieveActiveParticipants(conversationId);
        conversationParticipantDtos.stream()
                .filter(conversationParticipantDto -> filterSender(conversationParticipantDto, richMessageDto))
                .filter(this::filterMuted)
                .map(ConversationParticipantDto::getAccountId)
                .map(toAccountId -> map(richMessageDto, toAccountId))
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

    private NotificationSendVo map(RichMessageDto richMessageDto, String toAccountId) {
        NotificationSendVo notificationSendVo = new NotificationSendVo();

        notificationSendVo.setAccountId(toAccountId);
        notificationSendVo.setThreadId(richMessageDto.getThreadId());
        //TODO cgds-461
        notificationSendVo.setLaunchUrl("https://jinear.co/pricing");
        notificationSendVo.setIsSilent(retrieveIsSilent(toAccountId));
        notificationSendVo.setNotificationType(MESSAGING_NEW_MESSAGE_CONVERSATION);
        //todo: cgds-461 Test. Excluding message owner from notifications we probably shouldn't need this.
        //notificationSendVo.setSenderSessionId(null);

        Optional.of(richMessageDto)
                .map(RichMessageDto::getThread)
                .map(ThreadDto::getChannel)
                .map(PlainChannelDto::getWorkspaceId)
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
        return Boolean.TRUE.equals(accountCommunicationPermissionDto.getPushNotification());
    }
}
