package co.jinear.core.model.entity.project;

import co.jinear.core.converter.project.MilestoneStateTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.project.MilestoneStateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "milestone")
public class Milestone extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "milestone_id")
    private String milestoneId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "title")
    private String title;

    @Column(name = "description_rich_text_id")
    private String descriptionRichTextId;

    @Column(name = "milestone_order")
    private Integer milestoneOrder;

    @Column(name = "target_date")
    private ZonedDateTime targetDate;

    @Convert(converter = MilestoneStateTypeConverter.class)
    @Column(name = "milestone_state")
    private MilestoneStateType milestoneState;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "description_rich_text_id", referencedColumnName = "related_object_id", insertable = false, updatable = false)
    private RichText description;
}
