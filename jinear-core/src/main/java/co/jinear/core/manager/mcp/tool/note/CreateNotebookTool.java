package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.notebook.NotebookOperationManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpCreateNotebookInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNotebookAcknowledgementView;
import co.jinear.core.model.request.notebook.NotebookInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateNotebookTool implements McpTool {

    private final NotebookOperationManager notebookOperationManager;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("create_notebook")
                .title("Create a notebook")
                .description("Creates a notebook in a workspace. Add notes to it with create_note. Returns the notebook id.")
                .input(McpSchemaGenerator.forInput(McpCreateNotebookInput.class))
                .output(McpSchemaGenerator.acknowledgement("notebookId", "Id of the created notebook."))
                .write()
                .scopes(OauthScope.NOTES_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpCreateNotebookInput input = args.bind(McpCreateNotebookInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        NotebookInitializeRequest request = new NotebookInitializeRequest();
        request.setTitle(input.getTitle());
        request.setDescription(input.getDescription());
        Optional.ofNullable(input.getVisibility()).ifPresent(request::setVisibility);
        String notebookId = notebookOperationManager.initialize(request, input.getWorkspaceId()).getNotebookId();
        return McpToolResult.of(McpNotebookAcknowledgementView.of(notebookId));
    }
}
