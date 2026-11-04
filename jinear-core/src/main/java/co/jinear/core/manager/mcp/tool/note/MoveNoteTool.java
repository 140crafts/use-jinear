package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpNoteEditConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.note.NoteOperationManager;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpMoveNoteInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteMoveView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.note.NoteMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MoveNoteTool implements McpTool {

    private final NoteOperationManager noteOperationManager;
    private final NoteFilterManager noteFilterManager;
    private final McpNoteEditConverter mcpNoteEditConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("move_note")
                .title("Move a note")
                .description("Moves a note to another notebook, under another note, or to the top level. "
                             + "Child notes move with it. Returns the link to the note at its new place.")
                .input(McpSchemaGenerator.forInput(McpMoveNoteInput.class))
                .output(McpSchemaGenerator.forType(McpNoteMoveView.class))
                .write()
                .scopes(OauthScope.NOTES_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpMoveNoteInput input = args.bind(McpMoveNoteInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        changeNotebookIfPresent(input);
        NoteMoveRequest request = mcpNoteEditConverter.move(input);
        noteOperationManager.move(input.getWorkspaceId(), request);
        McpNoteMoveView view = new McpNoteMoveView();
        view.setNoteId(input.getNoteId());
        view.setUrl(movedNoteUrl(input));
        view.setOk(Boolean.TRUE);
        return McpToolResult.of(view);
    }

    private String movedNoteUrl(McpMoveNoteInput input) {
        NoteFilterRequest request = new NoteFilterRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setNoteId(input.getNoteId());
        List<NoteDto> notes = noteFilterManager.filter(request).getNoteDtoPageDto().getContent();
        return notes.stream()
                .findFirst()
                .map(NoteDto::getUrl)
                .orElse(null);
    }

    private void changeNotebookIfPresent(McpMoveNoteInput input) {
        if (Objects.nonNull(input.getNotebookId())) {
            noteOperationManager.changeNotebook(input.getWorkspaceId(), input.getNoteId(), input.getNotebookId());
        }
    }
}
