package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "task_list_entry")
public class TaskListEntry extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_list_entry_id")
    private String taskListEntryId;

    @Column(name = "task_list_id")
    private String taskListId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "order")
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "task_list_id", insertable = false, updatable = false)
    private TaskList taskList;

    @ManyToOne
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;
}
