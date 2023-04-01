package co.jinear.core.model.vo.team.member;

import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamMemberAddVo {
    private String accountId;
    private String teamId;
    private TeamMemberRoleType role;
}
