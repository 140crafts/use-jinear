package co.jinear.core.model.entity.workspace;

import co.jinear.core.converter.workspace.WorkspaceAccountRoleTypeConverter;
import co.jinear.core.converter.workspace.WorkspaceInvitationStatusConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "workspace_invitation")
public class WorkspaceInvitation extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_invitation_id")
    private String workspaceInvitationId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "initial_team_id")
    private String initialTeamId;

    @Column(name = "invited_by")
    private String invitedBy;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "email")
    private String email;

    @Convert(converter = WorkspaceAccountRoleTypeConverter.class)
    @Column(name = "for_role")
    private WorkspaceAccountRoleType forRole;

    @Convert(converter = WorkspaceInvitationStatusConverter.class)
    @Column(name = "status")
    private WorkspaceInvitationStatusType status;
}
