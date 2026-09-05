package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.task.TaskSearchRequest;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Objects;

/**
 * The generic {@code search} and {@code fetch} pair.
 * <p>
 * These are not a second way to do what the task tools already do. They are the standard
 * retrieval contract a host uses when it wants to cite sources: search returns
 * identifiers with user-openable URLs, and fetch returns the full text behind one of
 * them. A host that knows this contract can pull Jinear into an answer without knowing
 * anything about workspaces or teams.
 * <p>
 * Both are read only and cover every workspace the account belongs to, because a citing
 * host has no workspace to pass in.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompatibilityMcpTools {

    private static final String TASK_PREFIX = "task:";
    private static final String NOTE_PREFIX = "note:";
    private static final int MAX_RESULTS = 20;

    private final WorkspaceManager workspaceManager;
    private final TaskSearchManager taskSearchManager;
    private final NoteFilterManager noteFilterManager;
    private final FeProperties feProperties;

    @Bean
    public McpTool searchTool() {
        return SimpleMcpTool.named("search")
                .title("Search Jinear")
                .description("Searches tasks and note titles across every Jinear workspace the signed in account belongs to, "
                        + "and returns ids and links suitable for citation. "
                        + "Pass an id from here to fetch to read the full record. "
                        + "For filtering by status, assignee or dates, use list_tasks instead.")
                .input(McpJsonSchema.object()
                        .requiredString("query", "What to look for, in plain language.")
                        .build())
                .output(McpJsonSchema.object()
                        .objectArray("results", "Matching records, tasks first.", McpJsonSchema.object()
                                .string("id", "Opaque id to pass to fetch.")
                                .string("title", "Record title.")
                                .string("url", "Absolute Jinear URL a person can open.")
                                .build(), true)
                        .build())
                .readOnly()
                .scopes(OauthScope.TASKS_READ, OauthScope.NOTES_READ, OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    String query = McpToolArguments.of(arguments).requiredString("query");
                    ObjectNode result = McpShapes.object();
                    ArrayNode results = result.putArray("results");

                    var memberships = workspaceManager.retrieveAccountWorkspacesInternal(context.getAccountId()).getWorkspaces();
                    for (var membership : memberships) {
                        if (results.size() >= MAX_RESULTS || Objects.isNull(membership.getWorkspace())) {
                            break;
                        }
                        String workspaceId = membership.getWorkspace().getWorkspaceId();
                        String workspaceUsername = membership.getWorkspace().getUsername();
                        addTaskMatches(results, workspaceId, workspaceUsername, query);
                        addNoteMatches(results, workspaceId, query);
                    }
                    return McpToolResult.of(result);
                })
                .build();
    }

    @Bean
    public McpTool fetchTool() {
        return SimpleMcpTool.named("fetch")
                .title("Fetch a Jinear record")
                .description("Reads the full text of one record previously returned by search, given its id. "
                        + "Returns the title, the body as text, and a link a person can open.")
                .input(McpJsonSchema.object()
                        .requiredString("id", "An id returned by search.")
                        .build())
                .output(McpJsonSchema.object()
                        .string("id", "The id that was fetched.")
                        .string("title", "Record title.")
                        .string("text", "Record body.")
                        .string("url", "Absolute Jinear URL a person can open.")
                        .build())
                .readOnly()
                .scopes(OauthScope.TASKS_READ, OauthScope.NOTES_READ, OauthScope.WORKSPACE_READ)
                .handler((context, arguments) -> {
                    String id = McpToolArguments.of(arguments).requiredString("id");
                    if (id.startsWith(NOTE_PREFIX)) {
                        return fetchNote(context, id);
                    }
                    if (id.startsWith(TASK_PREFIX)) {
                        return McpToolResult.error("Task ids from search cannot be fetched directly. "
                                + "Use get_task with the workspace username, team tag and task number shown in the url.");
                    }
                    return McpToolResult.error("Unrecognised id. Pass an id exactly as search returned it.");
                })
                .build();
    }

    private void addTaskMatches(ArrayNode results, String workspaceId, String workspaceUsername, String query) {
        try {
            TaskSearchRequest request = new TaskSearchRequest();
            request.setWorkspaceId(workspaceId);
            request.setQuery(query);
            var page = taskSearchManager.searchTask(request, 0).getResult();
            for (TaskDto task : page.getContent()) {
                if (results.size() >= MAX_RESULTS) {
                    return;
                }
                ObjectNode node = results.addObject();
                node.put("id", TASK_PREFIX + task.getTaskId());
                node.put("title", task.getTitle());
                node.put("url", taskUrl(workspaceUsername, task));
            }
        } catch (RuntimeException exception) {
            // One workspace failing must not empty the whole result set.
            log.debug("[MCP] search skipped workspace {}: {}", workspaceId, exception.getMessage());
        }
    }

    private void addNoteMatches(ArrayNode results, String workspaceId, String query) {
        try {
            NoteFilterRequest request = new NoteFilterRequest();
            request.setWorkspaceId(workspaceId);
            var page = noteFilterManager.filter(request).getNoteDtoPageDto();
            String needle = query.toLowerCase(Locale.ROOT);
            for (NoteDto note : page.getContent()) {
                if (results.size() >= MAX_RESULTS) {
                    return;
                }
                if (Objects.isNull(note.getTitle()) || !note.getTitle().toLowerCase(Locale.ROOT).contains(needle)) {
                    continue;
                }
                ObjectNode node = results.addObject();
                node.put("id", NOTE_PREFIX + workspaceId + ":" + note.getNoteId());
                node.put("title", note.getTitle());
                node.put("url", feProperties.getHomeUrl());
            }
        } catch (RuntimeException exception) {
            log.debug("[MCP] search skipped notes in workspace {}: {}", workspaceId, exception.getMessage());
        }
    }

    private McpToolResult fetchNote(co.jinear.core.model.mcp.McpToolContext context, String id) {
        String[] parts = id.substring(NOTE_PREFIX.length()).split(":", 2);
        if (parts.length != 2) {
            return McpToolResult.error("Malformed note id. Pass an id exactly as search returned it.");
        }
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(parts[0]);
        request.setNoteId(parts[1]);
        context.setWorkspaceId(parts[0]);
        var page = noteFilterManager.filter(request).getNoteDtoPageDto();
        if (page.getContent().isEmpty()) {
            return McpToolResult.error("That note is no longer visible to you.");
        }
        NoteDto note = page.getContent().get(0);
        ObjectNode node = McpShapes.object();
        node.put("id", id);
        node.put("title", note.getTitle());
        node.put("text", Objects.isNull(note.getRichText()) ? "" : note.getRichText().getValue());
        node.put("url", feProperties.getHomeUrl());
        return McpToolResult.of(node);
    }

    private String taskUrl(String workspaceUsername, TaskDto task) {
        String reference = Objects.isNull(task.getTeam()) || Objects.isNull(task.getTeamTagNo())
                ? task.getTaskId()
                : task.getTeam().getTag() + "-" + task.getTeamTagNo();
        return feProperties.getTaskUrl()
                .replace("{workspaceName}", Objects.isNull(workspaceUsername) ? "" : workspaceUsername)
                .replace("{taskTag}", reference);
    }

}
