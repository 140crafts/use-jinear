package co.jinear.core.model.entity.messaging;

import co.jinear.core.converter.messaging.conversation.MessageTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.entity.robot.Robot;
import co.jinear.core.model.enumtype.messaging.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "robot_id")
    private String robotId;

    @Convert(converter = MessageTypeConverter.class)
    @Column(name = "message_type")
    private MessageType messageType;

    @Column(name = "rich_text_id")
    private String richTextId;

    @Column(name = "thread_id")
    private String threadId;

    @Column(name = "conversation_id")
    private String conversationId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "robot_id", insertable = false, updatable = false)
    private Robot robot;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "rich_text_id", insertable = false, updatable = false)
    private RichText richText;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "thread_id", insertable = false, updatable = false)
    private Thread thread;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @OneToMany(mappedBy = "message")
    @Where(clause = "passive_id is null")
    private Set<MessageData> messageData;

    @OneToMany(mappedBy = "message")
    @Where(clause = "passive_id is null")
    private Set<MessageReaction> messageReactions;
}
