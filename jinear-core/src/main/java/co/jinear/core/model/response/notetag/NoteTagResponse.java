package co.jinear.core.model.response.notetag;

import co.jinear.core.model.dto.notetag.NoteTagDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteTagResponse extends BaseResponse {

    @JsonProperty("data")
    private NoteTagDto noteTagDto;
}
