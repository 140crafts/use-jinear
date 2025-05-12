package co.jinear.core.model.response.project;

import co.jinear.core.model.dto.project.PublicProjectDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicProjectRetrieveResponse extends BaseResponse {

    @JsonProperty("data")
    private PublicProjectDto publicProjectDto;
}
