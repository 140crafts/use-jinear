package co.jinear.core.model.entity.note;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.richtext.RichText;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "note")
@FieldNameConstants
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "note_id")
    private String noteId;

    @Column(name = "notebook_id")
    private String notebookId;

    @Column(name = "workspace_id", nullable = false)
    private String workspaceId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "parent_note_id")
    private String parentNoteId;

    @Column(name = "title")
    private String title;

    @Column(name = "rich_text_id")
    private String richTextId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "parent_note_id", referencedColumnName = "note_id", insertable = false, updatable = false)
    private Note parent;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "notebook_id", insertable = false, updatable = false)
    private Notebook notebook;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "rich_text_id", insertable = false, updatable = false)
    private RichText richText;

    @OneToMany(mappedBy = "note", fetch = FetchType.EAGER)
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<NoteTagAssignment> noteTagAssignments;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "owner_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account owner;
}
