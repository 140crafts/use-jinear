package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardEntryInitializeRequest extends BaseRequest {

    @NotBlank
    private String taskBoardId;
    @NotBlank
    private String taskId;
}
