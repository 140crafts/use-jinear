package co.jinear.core.manager.mcp.tool.compatibility;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.richtext.RichTextPlainTextConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
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

import java.util.Optional;
import co.jinear.core.model.mcp.input.McpFetchInput;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchTool implements McpTool {

    private final NoteFilterManager noteFilterManager;
    private final TaskRetrieveManager taskRetrieveManager;
    private final FeProperties feProperties;
    private final RichTextPlainTextConverter richTextPlainTextConverter;

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
            return fetchTask(context, id);
        }
        return McpToolResult.error("mcp.error.fetch.unrecognised-id");
    }

    private McpToolResult fetchTask(McpToolContext context, String id) {
        TaskDto task = taskRetrieveManager.retrieve(id.substring(McpSearchIds.TASK_PREFIX.length())).getTaskDto();
        context.setWorkspaceId(task.getWorkspaceId());
        McpFetchedRecordView view = new McpFetchedRecordView();
        view.setId(id);
        view.setTitle(task.getTitle());
        view.setText(plainText(task.getDescription()));
        view.setUrl(task.getUrl());
        return McpToolResult.of(view);
    }

    private McpToolResult fetchNote(McpToolContext context, String id) {
        String[] parts = id.substring(McpSearchIds.NOTE_PREFIX.length()).split(":", 2);
        if (parts.length != 2) {
            return McpToolResult.error("mcp.error.fetch.malformed-note-id");
        }
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(parts[0]);
        request.setNoteId(parts[1]);
        context.setWorkspaceId(parts[0]);
        PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
        if (page.getContent().isEmpty()) {
            return McpToolResult.error("mcp.error.fetch.note-not-visible");
        }
        NoteDto note = page.getContent().get(0);
        McpFetchedRecordView view = new McpFetchedRecordView();
        view.setId(id);
        view.setTitle(note.getTitle());
        view.setText(plainText(note.getRichText()));
        String url = Optional.ofNullable(note.getUrl()).orElseGet(feProperties::getHomeUrl);
        view.setUrl(url);
        return McpToolResult.of(view);
    }

    private String plainText(RichTextDto richText) {
        String html = Optional.ofNullable(richText).map(RichTextDto::getValue).orElse(null);
        return richTextPlainTextConverter.toPlainText(html);
    }
}
