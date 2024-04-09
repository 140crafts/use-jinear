package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, String> {
}
