package co.jinear.core.model.entity.messaging;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "conversation_participant")
public class ConversationParticipant extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "conversation_participant_id")
    private String conversationParticipantId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "left_at")
    private ZonedDateTime leftAt;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
