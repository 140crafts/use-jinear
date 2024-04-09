package co.jinear.core.model.entity.messaging;

import co.jinear.core.converter.messaging.MessageReactionTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.enumtype.messaging.MessageReactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "message_reaction")
public class MessageReaction extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "message_reaction_id")
    private String messageReactionId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "account_id")
    private String accountId;

    @Convert(converter = MessageReactionTypeConverter.class)
    @Column(name = "reaction_type")
    private MessageReactionType reactionType;

    @Column(name = "unicode")
    private String unicode;

    @ManyToOne
    @JoinColumn(name = "message_id", insertable = false, updatable = false)
    private Message message;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
