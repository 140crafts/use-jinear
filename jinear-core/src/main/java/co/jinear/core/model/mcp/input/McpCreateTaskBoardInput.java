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
public class McpCreateTaskBoardInput extends McpWorkspaceScopedInput {

    @NotBlank
    @McpField(value = McpInputDescriptions.TEAM_ID, hint = "Call list_teams to find it.")
    private String teamId;

    @NotBlank
    @McpField("Board name, for example Sprint 14.")
    private String title;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("Instant the board's work is due.")
    private ZonedDateTime dueDate;
}
