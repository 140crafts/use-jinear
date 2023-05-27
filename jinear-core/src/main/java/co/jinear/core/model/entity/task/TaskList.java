package co.jinear.core.model.entity.task;

import co.jinear.core.converter.task.TaskListStateTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.task.TaskListStateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "task_list")
public class TaskList extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_list_id")
    private String taskListId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "state")
    @Convert(converter = TaskListStateTypeConverter.class)
    private TaskListStateType state;

    @OneToMany(mappedBy = "taskList")
    @Where(clause = "passive_id is null")
    @OrderBy("order asc")
    private Set<TaskListEntry> taskListEntries;
}
