package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.MessageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDataRepository extends JpaRepository<MessageData, String> {
}
