package co.jinear.core.model.entity.note;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "note_tag")
@FieldNameConstants
public class NoteTag extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "note_tag_id")
    private String noteTagId;

    @Column(name = "notebook_id", nullable = false)
    private String notebookId;

    @Column(name = "workspace_id", nullable = false)
    private String workspaceId;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "notebook_id", insertable = false, updatable = false)
    private Notebook notebook;
}
