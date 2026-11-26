package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.richtext.RichTextVersionManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpListNoteVersionsInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteVersionView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListNoteVersionsTool implements McpTool {

    private final NoteFilterManager noteFilterManager;
    private final RichTextVersionManager richTextVersionManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_note_versions")
                .title("List a note's versions")
                .description("Lists the saved versions of a note, newest first, 50 per page. "
                             + "The first item is the current version. "
                             + "Each version holds the edits of one editor in one session within about a minute. "
                             + "Returns ids, editors and dates; call get_note_version for the body of a version.")
                .input(McpSchemaGenerator.forInput(McpListNoteVersionsInput.class))
                .output(McpSchemaGenerator.page(McpNoteVersionView.class, "Versions of this note, newest first."))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListNoteVersionsInput input = args.bind(McpListNoteVersionsInput.class);
        int page = args.page();
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<NoteDto> notes = retrieveNote(input);
        if (notes.getContent().isEmpty()) {
            return McpToolResult.error("mcp.error.note.not-visible");
        }
        NoteDto note = notes.getContent().get(0);
        PageDto<RichTextVersionDto> versions = richTextVersionManager
                .retrieveVersions(note.getRichTextId(), page)
                .getRichTextVersionDtoPageDto();
        McpPageView<McpNoteVersionView> view = McpPageView.of(versions, mcpViewConverter::noteVersion);
        return McpToolResult.of(view);
    }

    private PageDto<NoteDto> retrieveNote(McpListNoteVersionsInput input) {
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setNoteId(input.getNoteId());
        return noteFilterManager.filter(request).getNoteDtoPageDto();
    }
}
