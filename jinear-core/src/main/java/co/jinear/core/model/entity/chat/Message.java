package co.jinear.core.model.entity.chat;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.richtext.RichText;
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
}
