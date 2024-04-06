package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, String> {
}
