package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MessageRepository extends JpaRepository<Message, String> {

    Page<Message> findAllByThreadIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateDesc(String threadId, Date createdDateBefore, Pageable pageable);

    Page<Message> findAllByConversationIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateDesc(String threadId, Date createdDateBefore, Pageable pageable);
}
