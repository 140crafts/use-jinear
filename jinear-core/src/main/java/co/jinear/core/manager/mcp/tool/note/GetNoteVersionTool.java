package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.richtext.RichTextVersionManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDetailDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpGetNoteVersionInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteVersionDetailView;
import co.jinear.core.model.mcp.view.McpNoteVersionEnvelopeView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNoteVersionTool implements McpTool {

    private final NoteFilterManager noteFilterManager;
    private final RichTextVersionManager richTextVersionManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_note_version")
                .title("Get one note version")
                .description("Reads one saved version of a note in full: its title and its body as HTML, in the same form as get_note. "
                             + "Use it to see what a note said earlier, to compare two versions, "
                             + "or to find text that was removed and put it back with replace_in_note.")
                .input(McpSchemaGenerator.forInput(McpGetNoteVersionInput.class))
                .output(McpSchemaGenerator.single("version", "The version, with its title and body.", McpNoteVersionDetailView.class))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetNoteVersionInput input = args.bind(McpGetNoteVersionInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<NoteDto> notes = retrieveNote(input);
        if (notes.getContent().isEmpty()) {
            return McpToolResult.error("mcp.error.note.not-visible");
        }
        NoteDto note = notes.getContent().get(0);
        RichTextVersionDetailDto version = richTextVersionManager
                .retrieveVersion(note.getRichTextId(), input.getVersionId())
                .getRichTextVersionDetailDto();
        McpNoteVersionDetailView view = mcpViewConverter.noteVersionDetail(version);
        return McpToolResult.of(McpNoteVersionEnvelopeView.of(view));
    }

    private PageDto<NoteDto> retrieveNote(McpGetNoteVersionInput input) {
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setNoteId(input.getNoteId());
        return noteFilterManager.filter(request).getNoteDtoPageDto();
    }
}
