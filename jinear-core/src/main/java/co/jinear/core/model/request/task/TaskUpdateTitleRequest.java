package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TaskUpdateTitleRequest extends BaseRequest {
    @NotNull
    private String title;
}
