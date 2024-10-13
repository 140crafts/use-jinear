package co.jinear.core.model.response.project;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.project.ProjectPostCommentDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectPostPaginatedCommentResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<ProjectPostCommentDto> projectPostCommentDtoPageDto;
}
