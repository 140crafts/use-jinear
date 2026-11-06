package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteOperationManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpNoteScopedInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteAcknowledgementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteNoteTool implements McpTool {

    private final NoteOperationManager noteOperationManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("delete_note")
                .title("Delete a note")
                .description("Deletes a note and all of its child notes.")
                .input(McpSchemaGenerator.forInput(McpNoteScopedInput.class))
                .output(McpSchemaGenerator.acknowledgement("noteId", "The note that was deleted."))
                .destructive()
                .scopes(OauthScope.NOTES_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpNoteScopedInput input = args.bind(McpNoteScopedInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        noteOperationManager.delete(input.getWorkspaceId(), input.getNoteId());
        return McpToolResult.of(McpNoteAcknowledgementView.of(input.getNoteId()));
    }
}
