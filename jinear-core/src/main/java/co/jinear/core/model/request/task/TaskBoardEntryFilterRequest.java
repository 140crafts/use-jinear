package co.jinear.core.model.request.task;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class TaskBoardEntryFilterRequest extends BaseRequest {

    @Nullable
    private List<String> topicIds;
    @Nullable
    private List<String> ownerIds;
    @Nullable
    private List<String> assigneeIds;
    @Nullable
    private List<String> workflowStatusIdList;
    @Nullable
    private List<TeamWorkflowStateGroup> workflowStateGroups;
    @Nullable
    private ZonedDateTime timespanStart;
    @Nullable
    private ZonedDateTime timespanEnd;
}
