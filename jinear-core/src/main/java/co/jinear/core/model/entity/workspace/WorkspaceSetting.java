package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.workspace.WorkspaceContentVisibilityType;
import co.jinear.core.model.enumtype.workspace.WorkspaceJoinType;
import co.jinear.core.model.enumtype.workspace.WorkspaceVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "workspace_setting")
public class WorkspaceSetting extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_setting_id")
    private String workspaceSettingId;

    @Column(name = "workspace_id", unique = true)
    private String workspaceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private WorkspaceVisibilityType visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_visibility")
    private WorkspaceContentVisibilityType contentVisibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_type")
    private WorkspaceJoinType joinType;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;
}