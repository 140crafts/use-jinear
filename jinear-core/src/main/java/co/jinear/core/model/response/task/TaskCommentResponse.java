package co.jinear.core.model.response.task;

import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommentResponse extends BaseResponse {

    @JsonProperty("data")
    private CommentDto commentDto;
}
