package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedTaskCommentResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<CommentDto> commentsPage;
}
