package co.jinear.core.manager.mcp.tool.compatibility;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpSearchHitView;
import co.jinear.core.model.mcp.view.McpSearchResultsView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.task.TaskSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import co.jinear.core.model.mcp.input.McpSearchInput;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchTool implements McpTool {

    private static final int MAX_RESULTS = 20;

    private final WorkspaceManager workspaceManager;
    private final TaskSearchManager taskSearchManager;
    private final NoteFilterManager noteFilterManager;
    private final FeProperties feProperties;
    private final McpLinkConverter mcpLinkConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("search")
                .title("Search Jinear")
                .description("Searches tasks and note titles across every Jinear workspace the signed in account belongs to, "
                             + "and returns ids and links suitable for citation. "
                             + "Pass an id from here to fetch to read the full record. "
                             + "Tasks created or edited in the last minute may not appear yet; use list_tasks to see them. "
                             + "For filtering by status, assignee or dates, use list_tasks instead.")
                .input(McpSchemaGenerator.forInput(McpSearchInput.class))
                .output(McpSchemaGenerator.arrayField("results", McpSearchHitView.class, "Matching records, tasks first."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ, OauthScope.NOTES_READ, OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        String query = args.bind(McpSearchInput.class).getQuery();
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
            String workspaceUsername = membership.getWorkspace().getUsername();
            addTaskMatches(results, workspaceId, workspaceUsername, query);
            addNoteMatches(results, workspaceId, query);
        }
        return McpToolResult.of(result);
    }

    private void addTaskMatches(List<McpSearchHitView> results, String workspaceId, String workspaceUsername, String query) {
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
                        mcpLinkConverter.taskUrl(workspaceUsername, task)));
            }
        } catch (RuntimeException exception) {
            log.debug("[MCP] search skipped workspace {}: {}", workspaceId, exception.getMessage());
        }
    }

    private void addNoteMatches(List<McpSearchHitView> results, String workspaceId, String query) {
        try {
            NoteFilterRequest request = new NoteFilterRequest();
            request.setWorkspaceId(workspaceId);
            PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
            String needle = query.toLowerCase(Locale.ROOT);
            for (NoteDto note : page.getContent()) {
                if (results.size() >= MAX_RESULTS) {
                    return;
                }
                if (Objects.isNull(note.getTitle()) || !note.getTitle().toLowerCase(Locale.ROOT).contains(needle)) {
                    continue;
                }
                results.add(searchHit(McpSearchIds.NOTE_PREFIX + workspaceId + ":" + note.getNoteId(),
                        note.getTitle(), feProperties.getHomeUrl()));
            }
        } catch (RuntimeException exception) {
            log.debug("[MCP] search skipped notes in workspace {}: {}", workspaceId, exception.getMessage());
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
