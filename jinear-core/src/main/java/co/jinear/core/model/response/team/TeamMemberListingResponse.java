package co.jinear.core.model.response.team;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<TeamMemberDto> teamMemberDtoList;
}
