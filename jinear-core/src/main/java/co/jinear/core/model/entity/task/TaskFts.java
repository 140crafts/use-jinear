package co.jinear.core.model.entity.task;

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
@Table(name = "mv_task_fts")
public class TaskFts {

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "passive_id")
    private String passiveId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "comments_meta")
    private String commentsMeta;

    @Column(name = "attachments_meta")
    private String attachmentsMeta;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;
}
