package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import co.jinear.core.model.mcp.schema.McpZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class McpUpdateTaskInput {

    @NotBlank
    @McpField(value = "Task id, from search_tasks, list_tasks or get_task.", hint = "Call search_tasks to find it.")
    private String taskId;

    @McpField("New one line summary.")
    private String title;

    @McpField("New task body. Plain text or simple HTML.")
    private String description;

    @McpField("Account id of the new assignee. Pass an empty string to unassign.")
    private String assignedTo;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("New start. Supply together with dueDate, since Jinear stores the pair.")
    private ZonedDateTime startDate;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("New due date. Supply together with startDate, since Jinear stores the pair.")
    private ZonedDateTime dueDate;
}
