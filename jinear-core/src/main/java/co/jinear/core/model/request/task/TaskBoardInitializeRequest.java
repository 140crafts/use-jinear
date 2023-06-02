package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardInitializeRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @NotBlank
    private String teamId;
    @NotBlank
    private String title;
    @Nullable
    private ZonedDateTime dueDate;
}
