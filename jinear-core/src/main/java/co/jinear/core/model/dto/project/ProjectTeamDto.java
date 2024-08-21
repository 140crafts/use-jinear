package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTeamDto extends BaseDto {

    private String projectTeamId;
    private String projectId;
    private String teamId;
    private TeamDto team;
}
