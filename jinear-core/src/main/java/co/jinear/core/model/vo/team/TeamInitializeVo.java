package co.jinear.core.model.vo.team;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamInitializeVo {

    private String initializedBy;
    private String workspaceId;
    private String name;
    private String username;
    private String tag;
    private TeamVisibilityType visibility;
    private TeamJoinMethodType joinMethod;
    private LocaleType locale;
}
