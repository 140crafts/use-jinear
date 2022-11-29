package co.jinear.core.model.vo.team;

import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
public class TeamInitializeVo {

    private String workspaceId;
    private String name;
    private String tag;
    private TeamVisibilityType visibility;
    private TeamJoinMethodType joinMethod;
}
