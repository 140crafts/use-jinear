package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}
