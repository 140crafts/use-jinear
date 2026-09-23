package co.jinear.core.manager.mcp.tool.file;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.model.dto.material.MaterialHierarchyDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpFileView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.jinear.core.model.mcp.input.McpListFilesInput;

@Service
@RequiredArgsConstructor
public class ListFilesTool implements McpTool {

    private final MaterialListingManager materialListingManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_files")
                .title("List files and folders")
                .description("Lists the contents of a workspace's file storage, one folder at a time. "
                             + "Omit parentFolderId to list the root. Returns both folders and files; "
                             + "pass a folder's materialId back as parentFolderId to descend into it.")
                .input(McpSchemaGenerator.forInput(McpListFilesInput.class))
                .output(McpSchemaGenerator.page(McpFileView.class, "Files and folders in this location."))
                .readOnly()
                .scopes(OauthScope.FILES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListFilesInput input = args.bind(McpListFilesInput.class);
        MaterialSearchRequest request = new MaterialSearchRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setParentMaterialId(input.getParentFolderId());
        request.setPage(args.page());
        request.setMaterialType(input.getType());
        context.setWorkspaceId(input.getWorkspaceId());
        MaterialHierarchyDto hierarchy = materialListingManager.search(request).getMaterialHierarchyDto();
        return McpToolResult.of(McpPageView.of(hierarchy.getContent(), mcpViewConverter::file));
    }
}
