package co.jinear.core.model.request.notetag;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NoteTagUpdateRequest extends BaseRequest {

    @NotBlank
    private String noteTagId;
    @NotBlank
    private String name;
    @Nullable
    private String color;
}
