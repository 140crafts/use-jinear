package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class TaskUpdateDescriptionRequest extends BaseRequest {
    @Nullable
    private String description;
}
