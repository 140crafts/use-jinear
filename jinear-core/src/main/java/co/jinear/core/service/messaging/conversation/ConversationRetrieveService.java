package co.jinear.core.service.messaging.conversation;

import co.jinear.core.converter.messaging.conversation.ConversationDtoConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationDto;
import co.jinear.core.repository.messaging.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationRetrieveService {

    private final ConversationRepository conversationRepository;
    private final ConversationDtoConverter conversationDtoConverter;

    public Optional<ConversationDto> retrieveWithParticipantsKey(String participantsKey) {
        log.info("Retrieve with participants hash has started. participantsKey: {}", participantsKey);
        return conversationRepository.findByParticipantsKeyAndPassiveIdIsNull(participantsKey)
                .map(conversationDtoConverter::convert);
    }
}
