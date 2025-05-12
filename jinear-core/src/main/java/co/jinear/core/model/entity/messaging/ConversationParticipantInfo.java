package co.jinear.core.model.entity.messaging;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_conversation_participant_info")
public class ConversationParticipantInfo {

    @Id
    @Column(name = "conversation_participant_id")
    private String conversationParticipantId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "unread_count")
    private Long unreadCount;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "conversation_participant_id", insertable = false, updatable = false)
    private ConversationParticipant conversationParticipant;
}
