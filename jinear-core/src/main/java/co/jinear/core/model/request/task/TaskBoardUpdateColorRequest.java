package co.jinear.core.model.request.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardUpdateColorRequest extends TaskBoardUpdateRequest {

    @NotNull
    @Size(max = 6)
    private String color;
}
