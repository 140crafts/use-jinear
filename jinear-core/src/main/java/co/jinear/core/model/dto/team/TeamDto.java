package co.jinear.core.model.dto.team;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TeamDto extends BaseDto {
    private String teamId;
    private String workspaceId;
    private String name;
    private String tag;
    private TeamVisibilityType visibility;
    private TeamJoinMethodType joinMethod;
}
