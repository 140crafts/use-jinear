package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
@ToString
public class AccountContextWorkspaceDto {

    private DetailedWorkspaceMemberDto membership;
    private Long memberCount;
    @Nullable
    private List<WorkspaceMemberDto> members;
    private List<AccountContextTeamDto> teams;
}
