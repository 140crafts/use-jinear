package co.jinear.core.model.response.team;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamMembershipsResponse extends BaseResponse {

    @JsonProperty("data")
    private List<TeamMemberDto> teamMemberDtoList;
}
