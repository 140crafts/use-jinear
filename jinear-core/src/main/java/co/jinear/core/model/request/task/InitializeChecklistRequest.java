package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString(callSuper = true)
public class InitializeChecklistRequest extends BaseRequest {

    @NotBlank
    private String taskId;
    @NotBlank
    private String title;
    @Nullable
    private String initialItemLabel;
}
