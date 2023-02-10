package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "workspace_activity")
public class WorkspaceActivity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_activity_id")
    private String workspaceActivityId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "task_id")
    private String taskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    private WorkspaceActivityType type;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Column(name = "old_state")
    private String oldState;

    @Column(name = "newState")
    private String newState;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "performed_by", insertable = false, updatable = false)
    private Account performedByAccount;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "related_object_id", insertable = false, updatable = false)
    private Account relatedAccount;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "related_object_id", insertable = false, updatable = false)
    private Task relatedTask;

}
