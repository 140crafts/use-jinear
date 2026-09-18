package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpViewConverter;
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
import co.jinear.core.model.mcp.view.McpNoteDetailView;
import co.jinear.core.model.mcp.view.McpNoteEnvelopeView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpGetNoteInput;

@Service
@RequiredArgsConstructor
public class GetNoteTool implements McpTool {

    private final NoteFilterManager noteFilterManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_note")
                .title("Get one note")
                .description("Reads a single note in full, including its body. "
                             + "Use it after search_notes when the contents of a specific note are needed.")
                .input(McpSchemaGenerator.forInput(McpGetNoteInput.class))
                .output(McpSchemaGenerator.single("note", "The note, with its body.", McpNoteDetailView.class))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetNoteInput input = args.bind(McpGetNoteInput.class);
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setNoteId(input.getNoteId());
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
        if (page.getContent().isEmpty()) {
            return McpToolResult.error("No note with that id is visible to you in this workspace. "
                                       + "Check noteId against search_notes.");
        }
        return McpToolResult.of(McpNoteEnvelopeView.of(mcpViewConverter.noteDetail(page.getContent().get(0))));
    }
}
