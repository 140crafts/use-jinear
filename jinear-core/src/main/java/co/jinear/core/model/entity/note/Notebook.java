package co.jinear.core.model.entity.note;

import co.jinear.core.converter.notebook.NotebookVisibilityTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
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
@Table(name = "notebook")
@FieldNameConstants
public class Notebook extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "notebook_id")
    private String notebookId;

    @Column(name = "workspace_id", nullable = false)
    private String workspaceId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Convert(converter = NotebookVisibilityTypeConverter.class)
    @Column(name = "visibility", nullable = false)
    private NotebookVisibilityType visibility;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "owner_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account owner;
}
