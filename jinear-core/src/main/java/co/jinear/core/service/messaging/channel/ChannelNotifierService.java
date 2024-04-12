package co.jinear.core.service.messaging.channel;

import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.channel.ChannelMemberDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
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

    @Async
    public void notifyChannelMembers(String channelId, RichMessageDto firstMessage) {
        log.info("Notify channel members has started. channelId: {}, firstMessage: {}", channelId, firstMessage);
        channelMemberListingService.retrieveChannelMembers(channelId)
                .stream()
                .filter(channelMemberDto -> filterSender(firstMessage, channelMemberDto))
                .filter(this::filterMuted)
                .map(ChannelMemberDto::getAccountId)
                .map(toAccountId -> map(firstMessage, toAccountId))
                .forEach(notificationCreateService::create);
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

    private NotificationSendVo map(RichMessageDto firstMessage, String toAccountId) {
        NotificationSendVo notificationSendVo = new NotificationSendVo();

        notificationSendVo.setAccountId(toAccountId);
        notificationSendVo.setThreadId(firstMessage.getThreadId());
        //TODO cgds-461
        notificationSendVo.setLaunchUrl("https://jinear.co/pricing");
        notificationSendVo.setIsSilent(retrieveIsSilent(toAccountId));
        notificationSendVo.setNotificationType(MESSAGING_NEW_MESSAGE_CONVERSATION);
        //todo: cgds-461 Test. Excluding message owner from notifications we probably shouldn't need this.
        //notificationSendVo.setSenderSessionId(null);

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
