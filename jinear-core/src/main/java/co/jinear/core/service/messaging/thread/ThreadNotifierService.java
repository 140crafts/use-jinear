package co.jinear.core.service.messaging.thread;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import co.jinear.core.service.client.messageapi.model.request.EmitRequest;
import co.jinear.core.service.messaging.emit.EmitterService;
import co.jinear.core.service.messaging.message.MessageListingService;
import co.jinear.core.service.notification.NotificationCreateService;
import co.jinear.core.service.richtext.HtmlSanitizeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static co.jinear.core.model.enumtype.notification.NotificationType.MESSAGING_NEW_MESSAGE_THREAD;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadNotifierService {

    private final MessageListingService messageListingService;
    private final NotificationCreateService notificationCreateService;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;
    private final HtmlSanitizeService htmlSanitizeService;
    private final EmitterService emitterService;
    private final ObjectMapper objectMapper;

    @Async
    public void notifyThreadParticipants(RichMessageDto richMessageDto) {
        log.info("Notify thread participants has started. richMessageDto: {}", richMessageDto);
        List<String> accountIds = messageListingService.retrieveAccountIdsParticipatedInThread(richMessageDto.getThreadId());
        emitMessage(richMessageDto, accountIds);
        mapAndSendNotification(richMessageDto, accountIds);
    }

    private void emitMessage(RichMessageDto richMessageDto, List<String> accountIds) {
        accountIds.stream()
                .map(toAccountId -> mapEmitRequest(richMessageDto, toAccountId))
                .forEach(emitterService::emitMessage);
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

    private void mapAndSendNotification(RichMessageDto richMessageDto, List<String> accountIds) {
        accountIds.stream()
                .filter(toAccountId -> !toAccountId.equals(richMessageDto.getAccountId()))
                .map(toAccountId -> map(richMessageDto, toAccountId))
                .forEach(notificationCreateService::create);
    }

    private NotificationSendVo map(RichMessageDto richMessageDto, String toAccountId) {
        NotificationSendVo notificationSendVo = new NotificationSendVo();

        notificationSendVo.setAccountId(toAccountId);
        notificationSendVo.setThreadId(richMessageDto.getThreadId());
        //TODO cgds-461
        notificationSendVo.setLaunchUrl("https://jinear.co/pricing");
        notificationSendVo.setIsSilent(retrieveIsSilent(toAccountId));
        notificationSendVo.setNotificationType(MESSAGING_NEW_MESSAGE_THREAD);
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
