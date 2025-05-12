package co.jinear.core.model.request.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardUpdateDueDateRequest extends TaskBoardUpdateRequest {

    @Nullable
    private ZonedDateTime dueDate;
}
