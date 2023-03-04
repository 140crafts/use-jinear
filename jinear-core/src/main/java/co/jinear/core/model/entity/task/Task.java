package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.entity.team.TeamWorkflowStatus;
import co.jinear.core.model.entity.topic.Topic;
import co.jinear.core.model.entity.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "topic_id")
    private String topicId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "workflow_status_id")
    private String workflowStatusId;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "assigned_date")
    private ZonedDateTime assignedDate;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "has_precise_assigned_date")
    private Boolean hasPreciseAssignedDate;

    @Column(name = "has_precise_due_date")
    private Boolean hasPreciseDueDate;

    @Column(name = "topic_tag_no")
    private Integer topicTagNo;

    @Column(name = "team_tag_no")
    private Integer teamTagNo;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workflow_status_id", insertable = false, updatable = false)
    private TeamWorkflowStatus workflowStatus;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "topic_id", insertable = false, updatable = false)
    private Topic topic;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private Account owner;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "assigned_to", insertable = false, updatable = false)
    private Account assignedToAccount;

    @OneToMany(mappedBy = "task")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<TaskRelation> relations;

    @OneToMany(mappedBy = "relatedTask")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<TaskRelation> relatedIn;

    @OneToMany(mappedBy = "task")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<TaskReminder> taskReminders;
}
