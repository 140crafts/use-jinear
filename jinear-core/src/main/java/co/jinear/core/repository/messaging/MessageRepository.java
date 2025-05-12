package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, String> {

    Optional<Message> findByMessageIdAndPassiveIdIsNull(String messageId);

    Page<Message> findAllByThreadIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateDesc(String threadId, Date createdDateBefore, Pageable pageable);

    Page<Message> findAllByConversationIdAndCreatedDateBeforeAndPassiveIdIsNullOrderByCreatedDateDesc(String threadId, Date createdDateBefore, Pageable pageable);

    @Query("select distinct(m.accountId) from Message m where m.threadId = :threadId and m.accountId is not null and m.passiveId is null")
    List<String> findDistinctThreadParticipantAccounts(@Param("threadId") String threadId);
}
