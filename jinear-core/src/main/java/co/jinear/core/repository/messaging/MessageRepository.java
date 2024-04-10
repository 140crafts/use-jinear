package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findAllByThreadIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateAsc(String threadId, ZonedDateTime createdDateBefore, Pageable pageable);
}
