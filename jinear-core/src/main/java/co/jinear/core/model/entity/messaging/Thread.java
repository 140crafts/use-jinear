package co.jinear.core.model.entity.messaging;

import co.jinear.core.converter.messaging.thread.ThreadTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.enumtype.messaging.ThreadType;
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
@Table(name = "thread")
public class Thread extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "thread_id")
    private String threadId;

    @Convert(converter = ThreadTypeConverter.class)
    @Column(name = "thread_type")
    private ThreadType threadType;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "last_activity_time")
    private ZonedDateTime lastActivityTime;

    @OneToOne(mappedBy = "thread")
    private ThreadMessageInfo threadMessageInfo;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "owner_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private Channel channel;
}
