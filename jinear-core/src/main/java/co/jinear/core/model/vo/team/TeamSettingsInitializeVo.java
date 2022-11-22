package co.jinear.core.model.vo.team;

import co.jinear.core.model.enumtype.team.TeamJoinType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamSettingsInitializeVo {
    private String teamId;
    private TeamVisibilityType visibility;
    private TeamJoinType joinType;
}
