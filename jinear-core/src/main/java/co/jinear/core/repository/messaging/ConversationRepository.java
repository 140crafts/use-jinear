package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface ConversationRepository extends JpaRepository<Conversation, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Conversation c
                set
                 c.lastActivityTime = :lastActivityTime,
                 c.lastUpdatedDate = current_timestamp()
                where
                     c.conversationId = :conversationId and
                     c.passiveId is null
                """)
    void updateLastActivityTime(@Param("conversationId") String conversationId,
                                @Param("lastActivityTime") ZonedDateTime lastActivityTime);

}
