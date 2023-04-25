package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "checklist")
public class Checklist extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "checklist_id")
    private String checklistId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @OneToMany(mappedBy = "checklist")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<ChecklistItem> checklistItems;
}
