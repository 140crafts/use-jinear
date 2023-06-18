package co.jinear.core.model.request.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class TaskFilterRequest {

    @Nullable
    private Integer page = 0;
    @NotBlank
    private String workspaceId;
    @NotEmpty
    private List<String> teamIdList;
    @Nullable
    private List<String> topicIds;
    @Nullable
    private List<String> ownerIds;
    @Nullable
    private List<String> assigneeIds;
    @Nullable
    private List<String> workflowStatusIdList;
    @Nullable
    private ZonedDateTime timespanStart;
    @Nullable
    private ZonedDateTime timespanEnd;
}
