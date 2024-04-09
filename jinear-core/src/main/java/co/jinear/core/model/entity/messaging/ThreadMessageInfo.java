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
@Table(name = "v_thread_message_info")
public class ThreadMessageInfo {

    @Id
    @Column(name = "thread_id")
    private String threadId;

    @Column(name = "initial_message_id")
    private String initialMessageId;

    @Column(name = "latest_message_id")
    private String latestMessageId;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "initial_message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    private Message initialMessage;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "latest_message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    private Message latestMessage;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "thread_id", insertable = false, updatable = false)
    private Thread thread;
}
