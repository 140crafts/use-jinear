package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "workspace_activity")
public class WorkspaceActivity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_activity_id")
    private String workspaceActivityId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    private WorkspaceActivityType type;
}
