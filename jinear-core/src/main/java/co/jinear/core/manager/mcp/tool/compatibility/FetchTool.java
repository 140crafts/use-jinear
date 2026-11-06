package co.jinear.core.manager.mcp.tool.compatibility;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.converter.richtext.RichTextPlainTextConverter;
import co.jinear.core.manager.material.MaterialManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpFetchInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpFetchedRecordView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchTool implements McpTool {

    private static final String UNKNOWN = "unknown";

    private final NoteFilterManager noteFilterManager;
    private final TaskRetrieveManager taskRetrieveManager;
    private final MaterialManager materialManager;
    private final FeProperties feProperties;
    private final RichTextPlainTextConverter richTextPlainTextConverter;
    private final McpLinkConverter mcpLinkConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("fetch")
                .title("Fetch a Jinear record")
                .description("Reads the full text of one record previously returned by search, given its id. "
                             + "Returns the title, the body as text, and a link a person can open. "
                             + "For a file, the text gives its type, size and creation date and the link downloads it; "
                             + "the contents of the file are not returned.")
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
        if (id.startsWith(McpSearchIds.FILE_PREFIX)) {
            return fetchFileIfGranted(context, id);
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

    private McpToolResult fetchFileIfGranted(McpToolContext context, String id) {
        if (!context.getScopes().contains(OauthScope.FILES_READ.getValue())) {
            return McpToolResult.error("mcp.error.fetch.files-not-granted");
        }
        return fetchFile(context, id);
    }

    private McpToolResult fetchFile(McpToolContext context, String id) {
        MaterialDto material = materialManager.retrieve(id.substring(McpSearchIds.FILE_PREFIX.length())).getMaterialDto();
        context.setWorkspaceId(material.getWorkspaceId());
        if (!MaterialType.FILE.equals(material.getMaterialType())) {
            return McpToolResult.error("mcp.error.file.is-folder");
        }
        McpFetchedRecordView view = new McpFetchedRecordView();
        view.setId(id);
        view.setTitle(material.getName());
        view.setText(fileText(material));
        view.setUrl(mcpLinkConverter.fileDownloadUrl(material.getMaterialId()));
        return McpToolResult.of(view);
    }

    private String fileText(MaterialDto material) {
        Optional<MediaDto> media = Optional.ofNullable(material.getMedia());
        String contentType = media.map(MediaDto::getContentType).orElse(UNKNOWN);
        String size = media.map(MediaDto::getSize).map(bytes -> bytes + " bytes").orElse(UNKNOWN);
        String createdAt = Optional.ofNullable(material.getCreatedDate())
                .map(date -> DateTimeFormatter.ISO_INSTANT.format(date.toInstant()))
                .orElse(UNKNOWN);
        return Stream.of("Type: " + contentType, "Size: " + size, "Created: " + createdAt)
                .collect(Collectors.joining("\n"));
    }

    private String plainText(RichTextDto richText) {
        String html = Optional.ofNullable(richText).map(RichTextDto::getValue).orElse(null);
        return richTextPlainTextConverter.toPlainText(html);
    }
}
