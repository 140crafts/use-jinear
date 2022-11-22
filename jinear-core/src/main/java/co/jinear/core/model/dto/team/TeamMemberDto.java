package co.jinear.core.model.dto.team;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class TeamMemberDto extends BaseDto {
    private String teamMemberId;
    private String teamId;
    private String accountId;
    private TeamAccountRoleType role;
    private AccountDto accountDto;
}
