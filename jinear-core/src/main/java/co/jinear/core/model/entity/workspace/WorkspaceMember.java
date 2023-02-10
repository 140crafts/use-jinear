package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "workspace_member")
public class WorkspaceMember extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_member_id")
    private String workspaceMemberId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "account_id")
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private WorkspaceAccountRoleType role;
}