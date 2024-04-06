package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, String> {
}
