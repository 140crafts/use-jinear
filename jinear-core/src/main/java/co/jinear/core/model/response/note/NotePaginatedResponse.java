package co.jinear.core.model.response.note;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotePaginatedResponse extends BaseResponse {

    @JsonProperty("data")
    PageDto<NoteDto> noteDtoPageDto;
}
