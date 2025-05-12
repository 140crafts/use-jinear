package co.jinear.core.model.vo.team.member;

import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberAddVo {
    private String accountId;
    private String teamId;
    private TeamMemberRoleType role;
}
