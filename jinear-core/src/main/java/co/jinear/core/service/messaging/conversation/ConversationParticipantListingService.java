package co.jinear.core.service.messaging.conversation;

import co.jinear.core.converter.messaging.conversation.ConversationParticipantDtoConverter;
import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.repository.messaging.ConversationParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationParticipantListingService {

    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationParticipantDtoConverter conversationParticipantDtoConverter;

    public List<ConversationParticipantDto> retrieveParticipations(String workspaceId, String accountId) {
        log.info("Retrieve participations has started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        return conversationParticipantRepository.findAllByConversation_WorkspaceIdAndAccountIdAndPassiveIdIsNullOrderByLastUpdatedDateDesc(workspaceId, accountId)
                .stream()
                .map(conversationParticipantDtoConverter::convert)
                .toList();
    }

    public List<ConversationParticipantDto> retrieveActiveParticipants(String conversationId) {
        log.info("Retrieve active participants has started. conversationId: {}", conversationId);
        return conversationParticipantRepository.findAllByConversationIdAndLeftAtIsNullAndPassiveIdIsNull(conversationId)
                .stream()
                .map(conversationParticipantDtoConverter::convert)
                .toList();
    }
}
