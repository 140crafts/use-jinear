package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountDeleteEligibilityDto {

    private List<DetailedWorkspaceMemberDto> workspacesWithActiveMembers;
    private boolean eligible;
}
