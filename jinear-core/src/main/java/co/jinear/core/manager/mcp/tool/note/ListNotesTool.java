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
import co.jinear.core.model.mcp.input.McpListNotesInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListNotesTool implements McpTool {

    private final NoteSearchManager noteSearchManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_notes")
                .title("List notes")
                .description("Lists the notes of a workspace that the signed in account can see, newest first, including notes created a moment ago. "
                             + "Covers every notebook, the account's drafts and every level of nesting unless it is narrowed to one notebook or to the children of one note. "
                             + "Use search_notes to find notes by the words in them. "
                             + "Returns titles and ids; call get_note for a note's body.")
                .input(McpSchemaGenerator.forInput(McpListNotesInput.class))
                .output(McpSchemaGenerator.page(McpNoteView.class, "Notes in this workspace, newest first."))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListNotesInput input = args.bind(McpListNotesInput.class);
        int page = args.page();
        context.setWorkspaceId(input.getWorkspaceId());
        NoteListRequest request = new NoteListRequest();
        request.setNotebookId(input.getNotebookId());
        request.setParentNoteId(input.getParentNoteId());
        PageDto<NoteDto> notes = noteSearchManager.listNotes(input.getWorkspaceId(), request, page).getNoteDtoPageDto();
        McpPageView<McpNoteView> view = McpPageView.of(notes, mcpViewConverter::note);
        return McpToolResult.of(view);
    }
}
