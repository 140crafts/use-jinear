package co.jinear.core.manager.mcp.tool.compatibility;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpSearchInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpSearchHitView;
import co.jinear.core.model.mcp.view.McpSearchResultsView;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.request.task.TaskSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchTool implements McpTool {

    private static final int MAX_RESULTS = 20;

    private final WorkspaceManager workspaceManager;
    private final TaskSearchManager taskSearchManager;
    private final NoteSearchManager noteSearchManager;
    private final MaterialListingManager materialListingManager;
    private final FeProperties feProperties;
    private final McpLinkConverter mcpLinkConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("search")
                .title("Search Jinear")
                .description("Searches tasks, the titles and bodies of notes, and file names, across every Jinear workspace the signed in account belongs to, "
                             + "by keyword: every word must appear, as a whole word or as the start of a word. "
                             + "Returns ids and links suitable for citation. "
                             + "Pass an id from here to fetch to read the full record. "
                             + "Records created or edited in the last few seconds may not appear yet; use list_tasks, list_notes or list_files to see them. "
                             + "Files appear only when the files:read permission was granted; folders never appear, use search_files for them. "
                             + "For filtering by status, assignee or dates, use list_tasks instead.")
                .input(McpSchemaGenerator.forInput(McpSearchInput.class))
                .output(McpSchemaGenerator.arrayField("results", McpSearchHitView.class, "Matching records: tasks, then notes, then files, for each workspace in turn."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ, OauthScope.NOTES_READ, OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        String query = args.bind(McpSearchInput.class).getQuery();
        boolean filesGranted = context.getScopes().contains(OauthScope.FILES_READ.getValue());
        McpSearchResultsView result = new McpSearchResultsView();
        List<McpSearchHitView> results = result.getResults();

        List<DetailedWorkspaceMemberDto> memberships = workspaceManager
                .retrieveAccountWorkspacesInternal(context.getAccountId())
                .getWorkspaces();
        for (DetailedWorkspaceMemberDto membership : memberships) {
            if (results.size() >= MAX_RESULTS || Objects.isNull(membership.getWorkspace())) {
                break;
            }
            String workspaceId = membership.getWorkspace().getWorkspaceId();
            addTaskMatches(results, workspaceId, query);
            addNoteMatches(results, workspaceId, query);
            addFileMatchesIfGranted(results, workspaceId, query, filesGranted);
        }
        return McpToolResult.of(result);
    }

    private void addTaskMatches(List<McpSearchHitView> results, String workspaceId, String query) {
        try {
            TaskSearchRequest request = new TaskSearchRequest();
            request.setWorkspaceId(workspaceId);
            request.setQuery(query);
            PageDto<TaskDto> page = taskSearchManager.searchTask(request, 0).getResult();
            for (TaskDto task : page.getContent()) {
                if (results.size() >= MAX_RESULTS) {
                    return;
                }
                results.add(searchHit(McpSearchIds.TASK_PREFIX + task.getTaskId(), task.getTitle(),
                        task.getUrl()));
            }
        } catch (RuntimeException exception) {
            log.debug("[MCP] search skipped workspace {}: {}", workspaceId, exception.getMessage());
        }
    }

    private void addNoteMatches(List<McpSearchHitView> results, String workspaceId, String query) {
        try {
            NoteSearchRequest request = new NoteSearchRequest();
            request.setQuery(query);
            PageDto<NoteDto> page = noteSearchManager.searchNotes(workspaceId, request, 0).getNoteDtoPageDto();
            for (NoteDto note : page.getContent()) {
                if (results.size() >= MAX_RESULTS) {
                    return;
                }
                String url = Optional.ofNullable(note.getUrl()).orElseGet(feProperties::getHomeUrl);
                results.add(searchHit(McpSearchIds.NOTE_PREFIX + workspaceId + ":" + note.getNoteId(),
                        note.getTitle(), url));
            }
        } catch (RuntimeException exception) {
            log.debug("[MCP] search skipped notes in workspace {}: {}", workspaceId, exception.getMessage());
        }
    }

    private void addFileMatchesIfGranted(List<McpSearchHitView> results, String workspaceId, String query, boolean filesGranted) {
        if (filesGranted) {
            addFileMatches(results, workspaceId, query);
        }
    }

    private void addFileMatches(List<McpSearchHitView> results, String workspaceId, String query) {
        try {
            MaterialTextSearchRequest request = new MaterialTextSearchRequest();
            request.setQuery(query);
            request.setMaterialType(MaterialType.FILE);
            PageDto<MaterialDto> page = materialListingManager.searchText(workspaceId, request, 0).getMaterialDtoPageDto();
            for (MaterialDto material : page.getContent()) {
                if (results.size() >= MAX_RESULTS) {
                    return;
                }
                results.add(searchHit(McpSearchIds.FILE_PREFIX + material.getMaterialId(), material.getName(),
                        mcpLinkConverter.fileDownloadUrl(material.getMaterialId())));
            }
        } catch (RuntimeException exception) {
            log.debug("[MCP] search skipped files in workspace {}: {}", workspaceId, exception.getMessage());
        }
    }

    private McpSearchHitView searchHit(String id, String title, String url) {
        McpSearchHitView hit = new McpSearchHitView();
        hit.setId(id);
        hit.setTitle(title);
        hit.setUrl(url);
        return hit;
    }
}
