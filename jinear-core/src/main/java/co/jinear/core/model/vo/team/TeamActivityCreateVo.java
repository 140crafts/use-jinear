package co.jinear.core.model.vo.team;

import co.jinear.core.model.enumtype.team.TeamActivityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamActivityCreateVo {
    private String teamId;
    private String accountId;
    private String relatedObjectId;
    private TeamActivityType type;
}
