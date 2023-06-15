package co.jinear.core.model.request.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class TaskFilterRequest {

    private int page = 0;
    @NotBlank
    private String workspaceId;
    @NotEmpty
    private List<String> teamIdList;
    private List<String> topicIds;
    private List<String> ownerIds;
    private List<String> assigneeIds;
    private List<String> workflowStatusIdList;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
}
