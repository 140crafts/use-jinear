package co.jinear.core.model.vo.team;

import co.jinear.core.model.enumtype.team.TeamJoinType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamInitializeVo {
    private String ownerId;
    private String title;
    private String description;
    private String handle;
    private TeamVisibilityType visibility;
    private TeamJoinType joinType;
    private Boolean appendRandomStrOnCollision;
}
