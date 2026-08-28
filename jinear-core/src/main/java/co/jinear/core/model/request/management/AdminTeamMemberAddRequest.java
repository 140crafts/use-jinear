package co.jinear.core.model.request.management;

import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdminTeamMemberAddRequest extends BaseRequest {

    @NotBlank
    private String accountId;
    @NotNull
    private TeamMemberRoleType role;
}
