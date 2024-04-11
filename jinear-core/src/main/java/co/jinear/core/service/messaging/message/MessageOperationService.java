package co.jinear.core.service.messaging.message;

import co.jinear.core.converter.messaging.message.MessageEntityConverter;
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

    @Transactional
    public void initialize(InitializeMessageVo initializeMessageVo) {
        log.info("Initialize message has started. initializeMessageVo: {}", initializeMessageVo);
        RichTextDto richTextDto = initializeRichText(initializeMessageVo);
        Message saved = saveMessage(initializeMessageVo, richTextDto);
        richTextInitializeService.updateRelatedObjectId(richTextDto.getRichTextId(), saved.getMessageId());
        initializeMessageData(initializeMessageVo, saved);
        updateConversationLastUpdateDateAsNow(saved);
        updateThreadConversationLastUpdateDateAsNow(saved);
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

    private Message saveMessage(InitializeMessageVo initializeMessageVo, RichTextDto richTextDto) {
        Message message = messageEntityConverter.convert(initializeMessageVo, richTextDto.getRichTextId());
        return messageRepository.save(message);
    }

    private RichTextDto initializeRichText(InitializeMessageVo initializeMessageVo) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setValue(initializeMessageVo.getBody());
        initializeRichTextVo.setType(RichTextType.MESSAGE);

        return richTextInitializeService.initializeRichText(initializeRichTextVo);
    }
}
