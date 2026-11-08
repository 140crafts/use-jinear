package co.jinear.core.manager.mcp.tool.file;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpSearchFilesInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpFileView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFilesTool implements McpTool {

    private final MaterialListingManager materialListingManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("search_files")
                .title("Search files and folders")
                .description("Searches the file storage of a workspace by name, original file name, file type and owner username, best matches first. "
                             + "Covers every folder at every level. Task attachments are not included; search_tasks covers them. "
                             + "Files added or renamed in the last few seconds may not appear yet; use list_files to see them. "
                             + "Returns files and folders with their ids; call get_file_link for a file's download link.")
                .input(McpSchemaGenerator.forInput(McpSearchFilesInput.class))
                .output(McpSchemaGenerator.page(McpFileView.class, "Matching files and folders."))
                .readOnly()
                .scopes(OauthScope.FILES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpSearchFilesInput input = args.bind(McpSearchFilesInput.class);
        int page = args.page();
        context.setWorkspaceId(input.getWorkspaceId());
        MaterialTextSearchRequest request = new MaterialTextSearchRequest();
        request.setQuery(input.getQuery());
        request.setMaterialType(input.getType());
        PageDto<MaterialDto> materials = materialListingManager.searchText(input.getWorkspaceId(), request, page).getMaterialDtoPageDto();
        McpPageView<McpFileView> view = McpPageView.of(materials, mcpViewConverter::file);
        return McpToolResult.of(view);
    }
}
