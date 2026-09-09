package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.mcp.schema.McpField;
import co.jinear.core.model.mcp.schema.McpZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class McpListTasksInput extends McpWorkspaceScopedInput {

    @McpField("Restrict to these teams.")
    private List<String> teamIds;

    @McpField("Restrict to tasks assigned to these account ids, from list_workspace_members.")
    private List<String> assigneeIds;

    @McpField("Restrict to these status ids, from list_workflow_statuses.")
    private List<String> workflowStatusIds;

    @McpField("Restrict by state group.")
    private List<TeamWorkflowStateGroup> stateGroups;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Only tasks whose dates fall on or after this instant.")
    private ZonedDateTime from;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Only tasks whose dates fall on or before this instant.")
    private ZonedDateTime to;

    @McpField(value = McpInputDescriptions.PAGE, minimum = 0)
    private Integer page;

    @McpField(value = McpInputDescriptions.PAGE_SIZE, minimum = 1)
    private Integer pageSize;
}
