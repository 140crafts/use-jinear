package co.jinear.core.model.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListUpdateTitleRequest extends TaskListUpdateRequest {

    @NotBlank
    private String title;
}
