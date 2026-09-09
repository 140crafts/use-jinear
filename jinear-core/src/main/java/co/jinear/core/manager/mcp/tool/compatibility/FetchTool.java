package co.jinear.core.manager.mcp.tool.compatibility;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpFetchedRecordView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import co.jinear.core.model.mcp.input.McpFetchInput;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchTool implements McpTool {

    private final NoteFilterManager noteFilterManager;
    private final FeProperties feProperties;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("fetch")
                .title("Fetch a Jinear record")
                .description("Reads the full text of one record previously returned by search, given its id. "
                             + "Returns the title, the body as text, and a link a person can open.")
                .input(McpSchemaGenerator.forInput(McpFetchInput.class))
                .output(McpSchemaGenerator.forType(McpFetchedRecordView.class))
                .readOnly()
                .scopes(OauthScope.TASKS_READ, OauthScope.NOTES_READ, OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        String id = args.bind(McpFetchInput.class).getId();
        if (id.startsWith(McpSearchIds.NOTE_PREFIX)) {
            return fetchNote(context, id);
        }
        if (id.startsWith(McpSearchIds.TASK_PREFIX)) {
            return McpToolResult.error("Task ids from search cannot be fetched directly. "
                                       + "Use get_task with the workspace username, team tag and task number shown in the url.");
        }
        return McpToolResult.error("Unrecognised id. Pass an id exactly as search returned it.");
    }

    private McpToolResult fetchNote(McpToolContext context, String id) {
        String[] parts = id.substring(McpSearchIds.NOTE_PREFIX.length()).split(":", 2);
        if (parts.length != 2) {
            return McpToolResult.error("Malformed note id. Pass an id exactly as search returned it.");
        }
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(parts[0]);
        request.setNoteId(parts[1]);
        context.setWorkspaceId(parts[0]);
        PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
        if (page.getContent().isEmpty()) {
            return McpToolResult.error("That note is no longer visible to you.");
        }
        NoteDto note = page.getContent().get(0);
        McpFetchedRecordView view = new McpFetchedRecordView();
        view.setId(id);
        view.setTitle(note.getTitle());
        view.setText(Objects.isNull(note.getRichText()) ? "" : note.getRichText().getValue());
        view.setUrl(feProperties.getHomeUrl());
        return McpToolResult.of(view);
    }
}
