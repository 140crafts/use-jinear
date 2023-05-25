package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskListUpdateDueDateRequest extends TaskListUpdateRequest {

    @Nullable
    private ZonedDateTime dueDate;
}
