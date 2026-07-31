package co.jinear.core.model.request.notetag;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignNoteTagRequest extends BaseRequest {

    @NotBlank
    private String noteId;
    @NotBlank
    private String noteTagId;
}
