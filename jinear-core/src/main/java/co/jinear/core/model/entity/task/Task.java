package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.topic.Topic;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "topic_id")
    private String topicId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "assigned_date")
    private ZonedDateTime assignedDate;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "tag_no")
    private Integer tagNo;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "main_task_id", insertable = false, updatable = false)
    private Task mainTask;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "topic_id", insertable = false, updatable = false)
    private Topic topic;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private Account account;
}
