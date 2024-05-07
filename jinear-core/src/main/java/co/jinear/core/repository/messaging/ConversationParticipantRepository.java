package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, String> {

    Optional<ConversationParticipant> findByConversationParticipantIdAndPassiveIdIsNull(String conversationParticipantId);

    boolean existsByConversationIdAndAccountIdAndLeftAtIsNullAndPassiveIdIsNull(String conversationId, String accountId);

    Optional<ConversationParticipant> findByConversationIdAndAccountIdAndLeftAtIsNullAndPassiveIdIsNull(String conversationId, String accountId);

    List<ConversationParticipant> findAllByConversation_WorkspaceIdAndAccountIdAndPassiveIdIsNullOrderByLastUpdatedDateDesc(String workspaceId, String accountId);

    List<ConversationParticipant> findAllByConversationIdAndLeftAtIsNullAndPassiveIdIsNull(String conversationId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ConversationParticipant cp
                set
                 cp.lastCheck = :lastCheck,
                 cp.lastUpdatedDate = current_timestamp()
                where
                     cp.conversationId = :conversationId and
                     cp.accountId = :accountId and
                     cp.passiveId is null
                """)
    void updateLastCheck(@Param("conversationId") String conversationId,
                         @Param("accountId") String accountId,
                         @Param("lastCheck") ZonedDateTime lastCheck);
}
