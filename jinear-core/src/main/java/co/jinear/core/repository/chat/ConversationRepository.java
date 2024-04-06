package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
}
