package co.jinear.core.model.response.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamResponse extends BaseResponse {

    @JsonProperty("data")
    private TeamDto teamDto;
}
