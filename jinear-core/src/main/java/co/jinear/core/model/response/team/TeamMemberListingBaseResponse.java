package co.jinear.core.model.response.team;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.TeamMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TeamMemberListingBaseResponse extends BaseResponse {
    @JsonProperty("data")
    private PageDto<TeamMemberDto> teamMemberDtoPage;
}
