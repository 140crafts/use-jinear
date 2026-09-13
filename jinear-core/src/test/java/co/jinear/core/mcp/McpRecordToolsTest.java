package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.manager.material.MaterialManager;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.compatibility.FetchTool;
import co.jinear.core.manager.mcp.tool.file.GetFileLinkTool;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpCommentView;
import co.jinear.core.model.mcp.view.McpFetchedRecordView;
import co.jinear.core.model.mcp.view.McpFileLinkView;
import co.jinear.core.model.mcp.view.McpTaskView;
import co.jinear.core.model.response.material.MaterialRetrieveResponse;
import co.jinear.core.model.response.task.TaskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class McpRecordToolsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final TaskRetrieveManager taskRetrieveManager = Mockito.mock(TaskRetrieveManager.class);
    private final MaterialManager materialManager = Mockito.mock(MaterialManager.class);

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    private FetchTool fetchTool() {
        FeProperties feProperties = new FeProperties();
        feProperties.setTaskUrl("https://jinear.co/{workspaceName}/task/{taskTag}");
        return new FetchTool(Mockito.mock(NoteFilterManager.class), taskRetrieveManager, feProperties,
                new McpLinkConverter(feProperties));
    }

    private GetFileLinkTool fileLinkTool() {
        return new GetFileLinkTool(materialManager, McpRealCatalog.oauthProperties());
    }

    private TaskResponse taskResponse() {
        TeamDto team = new TeamDto();
        team.setTag("ENG");
        WorkspaceDto workspace = new WorkspaceDto();
        workspace.setUsername("acme");
        RichTextDto description = new RichTextDto();
        description.setValue("<p>Body</p>");
        TaskDto task = new TaskDto();
        task.setTaskId("task-1");
        task.setWorkspaceId("workspace-1");
        task.setTitle("Ship the release");
        task.setTeam(team);
        task.setTeamTagNo(42);
        task.setWorkspace(workspace);
        task.setDescription(description);
        TaskResponse response = new TaskResponse();
        response.setTaskDto(task);
        return response;
    }

    private MaterialRetrieveResponse material(String materialId, MaterialType type) {
        MaterialDto material = new MaterialDto();
        material.setMaterialId(materialId);
        material.setWorkspaceId("workspace-1");
        material.setMaterialType(type);
        MaterialRetrieveResponse response = new MaterialRetrieveResponse();
        response.setMaterialDto(material);
        return response;
    }

    @Test
    void fetchReadsATaskIdReturnedBySearch() throws Exception {
        Mockito.when(taskRetrieveManager.retrieve("task-1")).thenReturn(taskResponse());
        McpToolContext context = context();

        McpToolResult result = fetchTool().call(context, args("{\"id\":\"task:task-1\"}"));

        McpFetchedRecordView view = (McpFetchedRecordView) result.getStructuredContent();
        assertThat(result.isError()).isFalse();
        assertThat(view.getId()).isEqualTo("task:task-1");
        assertThat(view.getTitle()).isEqualTo("Ship the release");
        assertThat(view.getText()).isEqualTo("<p>Body</p>");
        assertThat(view.getUrl()).isEqualTo("https://jinear.co/acme/task/ENG-42");
        assertThat(context.getWorkspaceId()).isEqualTo("workspace-1");
    }

    @Test
    void fetchStillRejectsAnIdSearchNeverReturns() throws Exception {
        McpToolResult result = fetchTool().call(context(), args("{\"id\":\"board:1\"}"));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).contains("Unrecognised id");
    }

    @Test
    void fileLinkRefusesAFolder() throws Exception {
        Mockito.when(materialManager.retrieve("folder-1")).thenReturn(material("folder-1", MaterialType.FOLDER));

        McpToolResult result = fileLinkTool().call(context(), args("{\"materialId\":\"folder-1\"}"));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).contains("folder");
    }

    @Test
    void fileLinkPointsAtTheMediaEndpointForAFile() throws Exception {
        Mockito.when(materialManager.retrieve("file-1")).thenReturn(material("file-1", MaterialType.FILE));
        McpToolContext context = context();

        McpToolResult result = fileLinkTool().call(context, args("{\"materialId\":\"file-1\"}"));

        McpFileLinkView view = (McpFileLinkView) result.getStructuredContent();
        assertThat(view.getUrl()).isEqualTo("https://api.jinear.co/v1/material/media/file-1");
        assertThat(context.getWorkspaceId()).isEqualTo("workspace-1");
    }

    @Test
    void fileLinkLetsAMissingOrHiddenRecordSurface() {
        Mockito.when(materialManager.retrieve("notebook-1")).thenThrow(new NotFoundException());

        assertThatThrownBy(() -> fileLinkTool().call(context(), args("{\"materialId\":\"notebook-1\"}")))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void aCommentCarriesTheIdOfTheCommentItQuotes() {
        CommentDto quoted = new CommentDto();
        quoted.setCommentId("comment-1");
        CommentDto reply = new CommentDto();
        reply.setCommentId("comment-2");
        reply.setQuote(quoted);

        McpCommentView view = new McpViewConverter().comment(reply);

        assertThat(view.getQuoteCommentId()).isEqualTo("comment-1");
    }

    @Test
    void aTaskPublishesItsStartDateUnderTheInputsName() {
        TaskDto task = new TaskDto();
        task.setAssignedDate(ZonedDateTime.parse("2026-09-18T09:00:00Z"));

        McpTaskView view = new McpViewConverter().task(task);

        assertThat(view.getStartDate()).isEqualTo("2026-09-18T09:00:00Z");
    }
}
