package co.jinear.core.model.request.notetag;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateNoteTagAssignmentsRequest extends BaseRequest {

    @NotBlank
    private String noteId;
    @NotNull
    private List<String> noteTagIds;
}
