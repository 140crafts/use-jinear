package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}
