package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpSearchNotesInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchNotesTool implements McpTool {

    private final NoteSearchManager noteSearchManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("search_notes")
                .title("Search notes")
                .description("Searches the notes of a workspace by the words in their titles and bodies, best matches first. "
                             + "Covers every notebook and every level of nesting unless it is narrowed to one notebook or to the children of one note. "
                             + "Notes created or edited in the last minute may not appear yet; use list_notes to see them. "
                             + "Returns titles and ids; call get_note for a note's body.")
                .input(McpSchemaGenerator.forInput(McpSearchNotesInput.class))
                .output(McpSchemaGenerator.page(McpNoteView.class, "Matching notes."))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpSearchNotesInput input = args.bind(McpSearchNotesInput.class);
        int page = args.page();
        context.setWorkspaceId(input.getWorkspaceId());
        NoteSearchRequest request = new NoteSearchRequest();
        request.setQuery(input.getQuery());
        request.setNotebookId(input.getNotebookId());
        request.setParentNoteId(input.getParentNoteId());
        PageDto<NoteDto> notes = noteSearchManager.searchNotes(input.getWorkspaceId(), request, page).getNoteDtoPageDto();
        McpPageView<McpNoteView> view = McpPageView.of(notes, mcpViewConverter::note);
        return McpToolResult.of(view);
    }
}
