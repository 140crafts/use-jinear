package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "task_board_entry")
public class TaskBoardEntry extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_board_entry_id")
    private String taskBoardEntryId;

    @Column(name = "task_board_id")
    private String taskBoardId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "order")
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "task_board_id", insertable = false, updatable = false)
    private TaskBoard taskBoard;

    @ManyToOne
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;
}
