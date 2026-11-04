package co.jinear.core.model.mcp.input;

import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import co.jinear.core.model.mcp.schema.McpField;
import co.jinear.core.model.mcp.schema.McpZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class McpUpdateTaskBoardInput {

    @NotBlank
    @McpField(value = "Board id, from list_task_boards.", hint = "Call list_task_boards to find it.")
    private String taskBoardId;

    @McpField("New board title.")
    private String title;

    @JsonDeserialize(using = McpZonedDateTimeDeserializer.class)
    @McpField("New due date of the board.")
    private ZonedDateTime dueDate;

    @McpField("True to remove the due date. Do not pass it together with dueDate.")
    private Boolean clearDueDate;

    @McpField("OPEN, or CLOSED when the work on the board is done.")
    private TaskBoardStateType state;

    @Size(max = 6)
    @McpField("New color as a hex value without #, for example FF6600.")
    private String color;
}
