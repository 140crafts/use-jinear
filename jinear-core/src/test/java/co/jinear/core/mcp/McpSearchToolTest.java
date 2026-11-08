package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.compatibility.SearchTool;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpSearchHitView;
import co.jinear.core.model.mcp.view.McpSearchResultsView;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.request.task.TaskSearchRequest;
import co.jinear.core.model.response.material.MaterialPaginatedResponse;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.response.task.TaskSearchResponse;
import co.jinear.core.model.response.workspace.AccountWorkspacesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class McpSearchToolTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final WorkspaceManager workspaceManager = Mockito.mock(WorkspaceManager.class);
    private final TaskSearchManager taskSearchManager = Mockito.mock(TaskSearchManager.class);
    private final NoteSearchManager noteSearchManager = Mockito.mock(NoteSearchManager.class);
    private final MaterialListingManager materialListingManager = Mockito.mock(MaterialListingManager.class);

    @Test
    void notesComeFromTheFullTextSearchAfterTheTasks() throws Exception {
        SearchTool tool = searchTool();
        Mockito.when(workspaceManager.retrieveAccountWorkspacesInternal("account-1")).thenReturn(workspaces("ws-1"));
        Mockito.when(taskSearchManager.searchTask(any(TaskSearchRequest.class), Mockito.eq(0))).thenReturn(tasks("task-1", "Release task"));
        Mockito.when(noteSearchManager.searchNotes(Mockito.eq("ws-1"), any(NoteSearchRequest.class), Mockito.eq(0)))
                .thenReturn(notes("note-1", "Notes about the release"));

        McpToolResult result = tool.call(context(), args("{\"query\": \"release\"}"));

        ArgumentCaptor<NoteSearchRequest> captor = ArgumentCaptor.forClass(NoteSearchRequest.class);
        Mockito.verify(noteSearchManager).searchNotes(Mockito.eq("ws-1"), captor.capture(), Mockito.eq(0));
        assertThat(captor.getValue().getQuery()).isEqualTo("release");
        McpSearchResultsView view = (McpSearchResultsView) result.getStructuredContent();
        assertThat(view.getResults()).extracting(McpSearchHitView::getId)
                .containsExactly("task:task-1", "note:ws-1:note-1");
        assertThat(view.getResults().get(1).getTitle()).isEqualTo("Notes about the release");
        assertThat(view.getResults().get(1).getUrl()).isEqualTo("https://app.jinear.co/acme/notebook/nb-1/note/note-1");
    }

    @Test
    void filesComeAfterTheNotesWithTheirDownloadLink() throws Exception {
        SearchTool tool = searchTool();
        Mockito.when(workspaceManager.retrieveAccountWorkspacesInternal("account-1")).thenReturn(workspaces("ws-1"));
        Mockito.when(taskSearchManager.searchTask(any(TaskSearchRequest.class), Mockito.eq(0))).thenReturn(tasks("task-1", "Hamzy task"));
        Mockito.when(noteSearchManager.searchNotes(Mockito.eq("ws-1"), any(NoteSearchRequest.class), Mockito.eq(0)))
                .thenReturn(notes("note-1", "Hamzy notes"));
        Mockito.when(materialListingManager.searchText(Mockito.eq("ws-1"), any(MaterialTextSearchRequest.class), Mockito.eq(0)))
                .thenReturn(files("file-1", "hamzy.pdf"));

        McpToolResult result = tool.call(filesContext(), args("{\"query\": \"hamzy\"}"));

        ArgumentCaptor<MaterialTextSearchRequest> captor = ArgumentCaptor.forClass(MaterialTextSearchRequest.class);
        Mockito.verify(materialListingManager).searchText(Mockito.eq("ws-1"), captor.capture(), Mockito.eq(0));
        assertThat(captor.getValue().getQuery()).isEqualTo("hamzy");
        assertThat(captor.getValue().getMaterialType()).isEqualTo(MaterialType.FILE);
        McpSearchResultsView view = (McpSearchResultsView) result.getStructuredContent();
        assertThat(view.getResults()).extracting(McpSearchHitView::getId)
                .containsExactly("task:task-1", "note:ws-1:note-1", "file:file-1");
        assertThat(view.getResults().get(2).getTitle()).isEqualTo("hamzy.pdf");
        assertThat(view.getResults().get(2).getUrl()).isEqualTo("https://api.jinear.co/v1/material/media/file-1");
    }

    @Test
    void filesAreLeftOutWithoutTheFilesPermission() throws Exception {
        SearchTool tool = searchTool();
        Mockito.when(workspaceManager.retrieveAccountWorkspacesInternal("account-1")).thenReturn(workspaces("ws-1"));
        Mockito.when(taskSearchManager.searchTask(any(TaskSearchRequest.class), Mockito.eq(0))).thenReturn(tasks("task-1", "Hamzy task"));
        Mockito.when(noteSearchManager.searchNotes(Mockito.eq("ws-1"), any(NoteSearchRequest.class), Mockito.eq(0)))
                .thenReturn(notes("note-1", "Hamzy notes"));

        McpToolResult result = tool.call(context(), args("{\"query\": \"hamzy\"}"));

        Mockito.verifyNoInteractions(materialListingManager);
        McpSearchResultsView view = (McpSearchResultsView) result.getStructuredContent();
        assertThat(view.getResults()).extracting(McpSearchHitView::getId)
                .containsExactly("task:task-1", "note:ws-1:note-1");
    }

    private SearchTool searchTool() {
        FeProperties feProperties = new FeProperties();
        feProperties.setHomeUrl("https://app.jinear.co");
        McpLinkConverter linkConverter = new McpLinkConverter(McpRealCatalog.oauthProperties());
        return new SearchTool(workspaceManager, taskSearchManager, noteSearchManager, materialListingManager, feProperties, linkConverter);
    }

    private McpToolContext filesContext() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of(OauthScope.FILES_READ.getValue())).build();
    }

    private MaterialPaginatedResponse files(String materialId, String name) {
        MaterialDto material = new MaterialDto();
        material.setMaterialId(materialId);
        material.setName(name);
        material.setMaterialType(MaterialType.FILE);
        PageImpl<MaterialDto> page = new PageImpl<>(List.of(material), PageRequest.of(0, 50), 1);
        MaterialPaginatedResponse response = new MaterialPaginatedResponse();
        response.setMaterialDtoPageDto(new PageDto<>(page));
        return response;
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    private AccountWorkspacesResponse workspaces(String workspaceId) {
        WorkspaceDto workspace = new WorkspaceDto();
        workspace.setWorkspaceId(workspaceId);
        DetailedWorkspaceMemberDto membership = new DetailedWorkspaceMemberDto();
        membership.setWorkspace(workspace);
        AccountWorkspacesResponse response = new AccountWorkspacesResponse();
        response.setWorkspaces(List.of(membership));
        return response;
    }

    private TaskSearchResponse tasks(String taskId, String title) {
        TaskDto task = new TaskDto();
        task.setTaskId(taskId);
        task.setTitle(title);
        PageImpl<TaskDto> page = new PageImpl<>(List.of(task), PageRequest.of(0, 50), 1);
        TaskSearchResponse response = new TaskSearchResponse();
        response.setResult(new PageDto<>(page));
        return response;
    }

    private NotePaginatedResponse notes(String noteId, String title) {
        NoteDto note = new NoteDto();
        note.setNoteId(noteId);
        note.setTitle(title);
        note.setUrl("https://app.jinear.co/acme/notebook/nb-1/note/" + noteId);
        PageImpl<NoteDto> page = new PageImpl<>(List.of(note), PageRequest.of(0, 50), 1);
        NotePaginatedResponse response = new NotePaginatedResponse();
        response.setNoteDtoPageDto(new PageDto<>(page));
        return response;
    }
}
