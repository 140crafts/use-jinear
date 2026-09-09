package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.task.ListTasksTool;
import co.jinear.core.manager.mcp.tool.task.UpdateTaskTool;
import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpCreateTaskInput;
import co.jinear.core.model.mcp.input.McpGetTaskInput;
import co.jinear.core.model.mcp.input.McpListTasksInput;
import co.jinear.core.model.mcp.schema.McpSchemaNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class McpToolInputTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    @Test
    void aDateArgumentAcceptsAnOffsetAndNotJustZulu() throws Exception {
        McpCreateTaskInput input = args("""
                {"workspaceId":"w","teamId":"t","title":"x","dueDate":"2026-08-29T14:00:00+02:00"}
                """).bind(McpCreateTaskInput.class);

        assertThat(input.getDueDate().withZoneSameInstant(ZoneOffset.UTC).getHour()).isEqualTo(12);
    }

    @Test
    void aDateArgumentStillAcceptsABareDate() throws Exception {
        McpCreateTaskInput input = args("""
                {"workspaceId":"w","teamId":"t","title":"x","startDate":"2026-08-29"}
                """).bind(McpCreateTaskInput.class);

        assertThat(input.getStartDate().toLocalDate().toString()).isEqualTo("2026-08-29");
    }

    @Test
    void aMissingRequiredArgumentReportsTheFieldsOwnHint() {
        assertThatThrownBy(() -> args("{\"workspaceUsername\":\"w\",\"teamTag\":\"ENG\"}")
                .bind(McpGetTaskInput.class))
                .isInstanceOf(McpToolException.class)
                .hasMessageContaining("taskNumber")
                .hasMessageContaining("In the reference ENG-42 it is 42.");
    }

    @Test
    void thePublishedPageSizeCeilingFollowsTheConfiguredOne() {
        McpProperties properties = new McpProperties();
        properties.setMaxPageSize(120);
        ListTasksTool tool = new ListTasksTool(Mockito.mock(TaskListingManager.class), properties, new McpViewConverter());

        McpSchemaNode pageSize = tool.definition().getInputSchema().getProperties().get("pageSize");

        assertThat(pageSize.getMaximum()).isEqualTo(120);
    }

    @Test
    void theStateGroupArgumentPublishesItsAllowedValues() {
        McpProperties properties = new McpProperties();
        properties.setMaxPageSize(50);
        ListTasksTool tool = new ListTasksTool(Mockito.mock(TaskListingManager.class), properties, new McpViewConverter());

        McpSchemaNode stateGroups = tool.definition().getInputSchema().getProperties().get("stateGroups");

        assertThat(stateGroups.getItems().getEnumValues())
                .containsExactly("BACKLOG", "NOT_STARTED", "STARTED", "COMPLETED", "CANCELLED");
    }

    @Test
    void updateTaskRefusesOneDateOnRatherThanClearingTheOther() throws Exception {
        TaskUpdateManager manager = Mockito.mock(TaskUpdateManager.class);
        UpdateTaskTool tool = new UpdateTaskTool(manager);

        McpToolResult result = tool.call(context(), args("""
                {"taskId":"task-1","dueDate":"2026-09-01"}
                """));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).contains("Supply startDate and dueDate together");
        Mockito.verifyNoInteractions(manager);
    }

    @Test
    void updateTaskAcceptsBothDatesTogether() throws Exception {
        TaskUpdateManager manager = Mockito.mock(TaskUpdateManager.class);
        UpdateTaskTool tool = new UpdateTaskTool(manager);

        McpToolResult result = tool.call(context(), args("""
                {"taskId":"task-1","startDate":"2026-09-01","dueDate":"2026-09-05"}
                """));

        assertThat(result.isError()).isFalse();
        Mockito.verify(manager).updateTaskDates(Mockito.eq("task-1"), Mockito.any());
    }

    @Test
    void updateTaskDoesNotCallTitleRequiredOnAnOptionalField() throws Exception {
        TaskUpdateManager manager = Mockito.mock(TaskUpdateManager.class);
        UpdateTaskTool tool = new UpdateTaskTool(manager);

        McpToolResult result = tool.call(context(), args("{\"taskId\":\"task-1\",\"title\":\"\"}"));

        assertThat(result.isError()).isFalse();
        Mockito.verify(manager).updateTaskTitle(Mockito.eq("task-1"), Mockito.any());
    }

    @Test
    void aToolIsConstructibleWithItsOwnDependenciesAlone() {
        UpdateTaskTool tool = new UpdateTaskTool(Mockito.mock(TaskUpdateManager.class));

        assertThat(tool.name()).isEqualTo("update_task");
    }
}
