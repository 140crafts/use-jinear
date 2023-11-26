package co.jinear.core.model.entity.google;

import co.jinear.core.converter.EncryptedColumnConverter;
import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "gmail_message")
public class GmailMessage extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "gmail_message_id")
    private String gmailMessageId;

    @Column(name = "gmail_thread_id")
    private String gmailThreadId;

    @Column(name="g_id")
    private String gId;

    @Column(name="g_thread_id")
    private String gThreadId;

    @Column(name="g_history_id")
    private String gHistoryId;

    @Column(name="g_internal_date")
    private String gInternalDate;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name="snippet")
    private String snippet;

    @Column(name="from")
    private String from;

    @Column(name="to")
    private String to;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name="subject")
    private String subject;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name="body")
    private String body;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "gmail_thread_id", insertable = false, updatable = false)
    private GmailThread gmailThread;
}
