package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpNoteEditConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.note.NoteContentManager;
import co.jinear.core.model.dto.note.NoteEditResultDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpUpdateNoteTitleInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNoteEditEnvelopeView;
import co.jinear.core.model.mcp.view.McpNoteEditView;
import co.jinear.core.model.vo.note.NoteEditVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateNoteTitleTool implements McpTool {

    private final NoteContentManager noteContentManager;
    private final McpNoteEditConverter mcpNoteEditConverter;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("update_note_title")
                .title("Update a note title")
                .description("Sets a new title on a note. The body stays as it is.")
                .input(McpSchemaGenerator.forInput(McpUpdateNoteTitleInput.class))
                .output(McpSchemaGenerator.single("edit", "The result of this write.", McpNoteEditView.class))
                .write()
                .scopes(OauthScope.NOTES_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpUpdateNoteTitleInput input = args.bind(McpUpdateNoteTitleInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        NoteEditVo vo = mcpNoteEditConverter.updateTitle(input);
        NoteEditResultDto result = noteContentManager.edit(vo);
        McpNoteEditView view = mcpViewConverter.noteEdit(result);
        return McpToolResult.of(McpNoteEditEnvelopeView.of(view));
    }
}
