package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskDetailView extends McpTaskView {

    @McpField("Task body as HTML, or null when the task has no body.")
    private String description;

    @McpField("Accounts that collaborate on the task.")
    private List<McpTaskCollaboratorView> collaborators;

    @McpField("Relations where this task is taskId, for example the parent of a subtask or a task this one blocks.")
    private List<McpTaskRelationView> relations;

    @McpField("Relations where this task is relatedTaskId, for example its subtasks.")
    private List<McpTaskRelationView> relatedIn;

    @McpField("Checklists of the task with their items.")
    private List<McpChecklistView> checklists;

    @McpField("Boards the task is on.")
    private List<McpTaskBoardEntryView> boards;
}
