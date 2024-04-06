package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.MessageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDataRepository extends JpaRepository<MessageData, String> {
}
