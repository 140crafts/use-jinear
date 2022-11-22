package co.jinear.core.model.vo.team;

import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeTeamMemberVo {
    private String accountId;
    private String teamId;
    private TeamAccountRoleType role;
}
