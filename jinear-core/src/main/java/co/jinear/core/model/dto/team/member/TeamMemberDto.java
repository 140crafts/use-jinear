package co.jinear.core.model.dto.team.member;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberDto extends BaseDto {

    private String teamMemberId;
    private String accountId;
    private String workspaceId;
    private String teamId;
    private TeamDto team;
    private AccountDto account;
}
