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
public class McpCreateTaskInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = McpInputDescriptions.TEAM_ID, hint = "Call list_teams to find it.")
    private String teamId;

    @NotBlank
    @McpField("One line summary of the work.")
    private String title;

    @McpField("Task body. Plain text or simple HTML.")
    private String description;

    @McpField("Account id of the assignee, from list_workspace_members.")
    private String assignedTo;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Instant the work should start.")
    private ZonedDateTime startDate;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Instant the work is due.")
    private ZonedDateTime dueDate;

    @McpField("Label to apply, from list_topics on the team.")
    private String topicId;

    @McpField("Board to add the task to on creation.")
    private String boardId;
}
