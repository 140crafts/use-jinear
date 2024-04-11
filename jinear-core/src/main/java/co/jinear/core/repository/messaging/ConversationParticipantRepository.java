package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, String> {

    @Query("""
            select cp.conversationId
            from ConversationParticipant cp
            where
            cp.accountId in :accountIds and
            cp.leftAt is null and
            cp.passiveId is null and
            cp.conversationId not in (select cp2.conversationId from ConversationParticipant cp2
                                                                where cp2.accountId not in (:accountIds) and
                                                                cp2.leftAt is null and
                                                                cp2.passiveId is null)
            """)
    String findConversationIdBetweenParticipants(@Param("accountIds") List<String> accountIds);

    Optional<ConversationParticipant> findByConversationParticipantIdAndPassiveIdIsNull(String conversationParticipantId);

    boolean existsByConversationIdAndAccountIdAndLeftAtIsNullAndPassiveIdIsNull(String conversationId, String accountId);

    List<ConversationParticipant> findAllByConversation_WorkspaceIdAndAccountIdAndPassiveIdIsNullOrderByLastUpdatedDateDesc(String workspaceId, String accountId);
}
