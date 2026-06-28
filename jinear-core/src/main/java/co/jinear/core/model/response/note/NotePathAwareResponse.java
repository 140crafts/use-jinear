package co.jinear.core.model.response.note;

import co.jinear.core.model.dto.note.NoteHierarchyDto;
import co.jinear.core.model.dto.note.PathAwareNoteDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotePathAwareResponse extends BaseResponse {

    @JsonProperty("data")
    private NoteHierarchyDto noteHierarchyDto;
}
