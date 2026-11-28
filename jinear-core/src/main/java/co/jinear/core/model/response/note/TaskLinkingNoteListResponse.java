package co.jinear.core.model.response.note;

import co.jinear.core.model.dto.note.TaskLinkingNoteDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskLinkingNoteListResponse extends BaseResponse {

    @JsonProperty("data")
    private List<TaskLinkingNoteDto> taskLinkingNoteDtos;
}
