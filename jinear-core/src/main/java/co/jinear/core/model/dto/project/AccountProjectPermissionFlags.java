package co.jinear.core.model.dto.project;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountProjectPermissionFlags {
    boolean isAccountWorkspaceAdminOrOwner;
    boolean isAccountIsProjectTeamsMember;
    boolean isAccountIsProjectTeamsAdmin;
    boolean canInitializePost;
    boolean canComment;
}
