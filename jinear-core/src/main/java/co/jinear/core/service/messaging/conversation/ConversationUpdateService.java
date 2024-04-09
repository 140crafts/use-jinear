package co.jinear.core.service.messaging.conversation;

import co.jinear.core.repository.messaging.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationUpdateService {

    private final ConversationRepository conversationRepository;

    public void updateLastActivityTimeAsNow(String conversationId) {
        log.info("Update last activity time as now has started. conversationId: {}", conversationId);
        conversationRepository.updateLastActivityTime(conversationId, ZonedDateTime.now());
    }
}
