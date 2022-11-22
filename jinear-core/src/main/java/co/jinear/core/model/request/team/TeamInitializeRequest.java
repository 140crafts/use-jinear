package co.jinear.core.model.request.team;

import co.jinear.core.model.enumtype.team.TeamJoinType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class TeamInitializeRequest extends BaseRequest {
    @NotBlank
    private String title;
    private String description;
    @Size(min = 3)
    @Size(max = 255)
    private String handle;
    @NotNull
    private TeamVisibilityType visibility;
    @NotNull
    private TeamJoinType joinType;
}
