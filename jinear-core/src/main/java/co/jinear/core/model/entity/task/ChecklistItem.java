package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "checklist_item")
public class ChecklistItem extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "checklist_item_id")
    private String checklistItemId;

    @Column(name = "checklist_id")
    private String checklistId;

    @Column(name = "label")
    private String label;

    @Column(name = "is_checked")
    private Boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "checklist_id", insertable = false, updatable = false)
    private Checklist checklist;
}
