package co.jinear.core.service.messaging.message;

import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.converter.messaging.message.MessageEntityConverter;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.messaging.Message;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.messaging.MessageRepository;
import co.jinear.core.service.messaging.conversation.ConversationUpdateService;
import co.jinear.core.service.messaging.thread.ThreadUpdateService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageOperationService {

    private final MessageRepository messageRepository;
    private final RichTextInitializeService richTextInitializeService;
    private final MessageEntityConverter messageEntityConverter;
    private final MessageDataOperationService messageDataOperationService;
    private final ConversationUpdateService conversationUpdateService;
    private final ThreadUpdateService threadUpdateService;
    private final MessageDtoConverter messageDtoConverter;

    @Transactional
    public RichMessageDto initialize(InitializeMessageVo initializeMessageVo) {
        log.info("Initialize message has started. initializeMessageVo: {}", initializeMessageVo);
        Optional<RichTextDto> richTextDtoOptional = initializeRichText(initializeMessageVo);
        Message saved = saveMessage(initializeMessageVo, richTextDtoOptional);
        updateRichTextRelatedObjectId(richTextDtoOptional, saved);
        initializeMessageData(initializeMessageVo, saved);
        updateConversationLastUpdateDateAsNow(saved);
        updateThreadConversationLastUpdateDateAsNow(saved);
        return messageDtoConverter.convertRich(saved);
    }

    private void initializeMessageData(InitializeMessageVo initializeMessageVo, Message saved) {
        Optional.of(initializeMessageVo)
                .map(InitializeMessageVo::getData)
                .ifPresent(data -> messageDataOperationService.initializeAll(saved.getMessageId(), data));
    }

    private void updateThreadConversationLastUpdateDateAsNow(Message message) {
        Optional.of(message)
                .map(Message::getThreadId)
                .ifPresent(threadUpdateService::updateLastActivityTimeAsNow);
    }

    private void updateConversationLastUpdateDateAsNow(Message message) {
        Optional.of(message)
                .map(Message::getConversationId)
                .ifPresent(conversationUpdateService::updateLastActivityTimeAsNow);
    }

    private Message saveMessage(InitializeMessageVo initializeMessageVo, Optional<RichTextDto> richTextDtoOptional) {
        Message message = messageEntityConverter.convert(initializeMessageVo, richTextDtoOptional);
        return messageRepository.save(message);
    }

    private Optional<RichTextDto> initializeRichText(InitializeMessageVo initializeMessageVo) {
        return Optional.of(initializeMessageVo)
                .map(InitializeMessageVo::getBody)
                .filter(body -> !body.isBlank())
                .map(this::mapInitializeRichTextVo)
                .map(richTextInitializeService::initializeRichText);
    }

    private InitializeRichTextVo mapInitializeRichTextVo(String body) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setValue(body);
        initializeRichTextVo.setType(RichTextType.MESSAGE);
        return initializeRichTextVo;
    }

    private void updateRichTextRelatedObjectId(Optional<RichTextDto> richTextDtoOptional, Message saved) {
        richTextDtoOptional.map(RichTextDto::getRichTextId)
                .ifPresent(richTextId -> richTextInitializeService.updateRelatedObjectId(richTextId, saved.getMessageId()));
    }
}
