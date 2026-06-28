package co.jinear.core.model.entity.note;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "note_tag_assignment")
@FieldNameConstants
public class NoteTagAssignment extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "note_tag_assignment_id")
    private String noteTagAssignmentId;

    @Column(name = "note_id", nullable = false)
    private String noteId;

    @Column(name = "note_tag_id", nullable = false)
    private String noteTagId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "note_tag_id", insertable = false, updatable = false)
    private NoteTag noteTag;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "note_id", insertable = false, updatable = false)
    private Note note;
}
