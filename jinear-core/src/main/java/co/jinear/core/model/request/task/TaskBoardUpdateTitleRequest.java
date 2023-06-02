package co.jinear.core.model.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardUpdateTitleRequest extends TaskBoardUpdateRequest {

    @NotBlank
    private String title;
}
