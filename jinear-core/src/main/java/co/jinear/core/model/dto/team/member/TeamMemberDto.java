package co.jinear.core.model.dto.team.member;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberDto extends BaseDto {

    private String teamMemberId;
    private String accountId;
    private String workspaceId;
    private String teamId;
    private TeamMemberRoleType role;
    private TeamDto team;
    private AccountDto account;
}
