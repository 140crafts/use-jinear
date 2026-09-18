package co.jinear.core.manager.mcp.tool.note;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpNotebookView;
import co.jinear.core.model.mcp.view.McpPageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpListNotebooksInput;

@Service
@RequiredArgsConstructor
public class ListNotebooksTool implements McpTool {

    private final NotebookListingManager notebookListingManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_notebooks")
                .title("List notebooks")
                .description("Lists the notebooks in a workspace. A notebook groups related notes, "
                             + "and its id is what narrows search_notes to one place.")
                .input(McpSchemaGenerator.forInput(McpListNotebooksInput.class))
                .output(McpSchemaGenerator.page(McpNotebookView.class, "Notebooks in this workspace."))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListNotebooksInput input = args.bind(McpListNotebooksInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        PageDto<NotebookDto> page = notebookListingManager
                .listWorkspaceNotebooks(input.getWorkspaceId(), args.page())
                .getNotebookDtoPageDto();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::notebook));
    }
}
