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
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import co.jinear.core.model.mcp.input.McpSearchNotesInput;

@Service
@RequiredArgsConstructor
public class SearchNotesTool implements McpTool {

    private final NoteFilterManager noteFilterManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("search_notes")
                .title("Search notes by title")
                .description("Lists notes in a workspace, optionally inside one notebook or under one parent note, "
                             + "and optionally narrowed to titles containing a phrase. "
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
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setNotebookId(input.getNotebookId());
        request.setParentNoteId(input.getParentNoteId());
        request.setPage(args.page());
        context.setWorkspaceId(input.getWorkspaceId());

        PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
        String titleContains = input.getTitleContains();
        if (Objects.isNull(titleContains) || titleContains.isBlank()) {
            return McpToolResult.of(McpPageView.of(page, mcpViewConverter::note));
        }
        return McpToolResult.of(McpPageView.of(page, matching(page, titleContains)));
    }

    private List<McpNoteView> matching(PageDto<NoteDto> page, String titleContains) {
        String needle = titleContains.toLowerCase(Locale.ROOT);
        return page.getContent().stream()
                .filter(note -> Objects.nonNull(note.getTitle())
                                && note.getTitle().toLowerCase(Locale.ROOT).contains(needle))
                .map(mcpViewConverter::note)
                .toList();
    }
}
