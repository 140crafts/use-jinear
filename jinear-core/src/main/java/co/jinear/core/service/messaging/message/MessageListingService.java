package co.jinear.core.service.messaging.message;

import co.jinear.core.converter.messaging.message.MessageDtoConverter;
import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.repository.messaging.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListingService {

    private static final int PAGE_SIZE = 250;
//    private static final int PAGE_SIZE = 5;

    private final MessageRepository messageRepository;
    private final MessageDtoConverter messageDtoConverter;

    public Page<MessageDto> retrieveThreadMessagesBefore(String threadId, Date before) {
        log.info("Retrieve thread messages before has started. threadId: {}, before: {}", threadId, before);
        return messageRepository.findAllByThreadIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateDesc(threadId, before, PageRequest.of(0, PAGE_SIZE))
                .map(messageDtoConverter::convert);
    }

    public Page<MessageDto> retrieveConversationMessagesBefore(String conversationId, Date before) {
        log.info("Retrieve conversation messages before has started. conversationId: {}, before: {}", conversationId, before);
        return messageRepository.findAllByConversationIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateDesc(conversationId, before, PageRequest.of(0, PAGE_SIZE))
                .map(messageDtoConverter::convert);
    }

    public List<String> retrieveAccountIdsParticipatedInThread(String threadId) {
        log.info("Retrieve account ids participated in thread has started. threadId: {}", threadId);
        return messageRepository.findDistinctThreadParticipantAccounts(threadId);
    }
}
