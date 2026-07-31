package co.jinear.core.model.response.notetag;

import co.jinear.core.model.dto.notetag.NoteTagDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteTagListingResponse extends BaseResponse {

    @JsonProperty("data")
    private List<NoteTagDto> noteTagDtoList;
}
