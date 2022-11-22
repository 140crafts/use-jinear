package co.jinear.core.model.dto.team;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.team.TeamContentVisibilityType;
import co.jinear.core.model.enumtype.team.TeamJoinType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamSettingDto extends BaseDto {

    private TeamVisibilityType visibility;
    private TeamContentVisibilityType contentVisibility;
    private TeamJoinType joinType;
}
