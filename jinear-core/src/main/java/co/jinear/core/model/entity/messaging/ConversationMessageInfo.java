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
@Table(name = "v_conversation_message_info")
public class ConversationMessageInfo {

    @Id
    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "last_message_id")
    private String lastMessageId;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "last_message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    private Message lastMessage;
}
