package co.jinear.core.model.request.team;

import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddTeamMemberRequest extends BaseRequest {

    @NotBlank
    private String accountId;
    @NotBlank
    private String teamId;
    @NotNull
    private TeamMemberRoleType role;
}
