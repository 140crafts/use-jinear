package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.task.TaskRelationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Getter
@Setter
@Entity
@Table(name = "task_relation")
public class TaskRelation extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_relation_id")
    private String taskRelationId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "related_task_id")
    private String relatedTaskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type")
    private TaskRelationType relationType;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "related_task_id", insertable = false, updatable = false)
    private Task relatedTask;
}