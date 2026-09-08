package co.jinear.core.manager.mcp.tool.config;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import co.jinear.core.model.dto.material.MaterialHierarchyDto;
import co.jinear.core.model.mcp.view.McpFileLinkView;
import co.jinear.core.model.mcp.view.McpFileView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.converter.mcp.McpViewConverter;

@Configuration
@RequiredArgsConstructor
public class FileMcpTools {

    private final MaterialListingManager materialListingManager;
    private final OauthProperties oauthProperties;
    private final McpViewConverter mcpViewConverter;

    @Bean
    public McpTool listFilesTool() {
        return SimpleMcpTool
                .named("list_files")
                .title("List files and folders")
                .description("Lists the contents of a workspace's file storage, one folder at a time. "
                             + "Omit parentFolderId to list the root. Returns both folders and files; "
                             + "pass a folder's materialId back as parentFolderId to descend into it.")
                .input(McpJsonSchema
                        .object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .string("parentFolderId", "Folder to list. Omit for the root of the workspace.")
                        .enumeration("type", "Return only FOLDER entries or only FILE entries. Omit for both.", List.of("FOLDER", "FILE"), false)
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpSchemaGenerator.page(McpFileView.class, "Files and folders in this location."))
                .readOnly()
                .scopes(OauthScope.FILES_READ)
                .handler((context, args) -> {
                    MaterialSearchRequest request = new MaterialSearchRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setParentMaterialId(args.optionalString("parentFolderId", null));
                    request.setPage(args.page());
                    request.setMaterialType(args.optionalEnum("type", MaterialType.class, "must be FOLDER or FILE."));
                    context.setWorkspaceId(request.getWorkspaceId());
                    MaterialHierarchyDto hierarchy = materialListingManager.search(request).getMaterialHierarchyDto();
                    return McpToolResult.of(McpPageView.of(hierarchy.getContent(), mcpViewConverter::file));
                })
                .build();
    }

    @Bean
    public McpTool getFileLinkTool() {
        return SimpleMcpTool.named("get_file_link")
                .title("Get a link to a file")
                .description("Returns the Jinear download link for a stored file, suitable for citing back to the person "
                             + "or for them to click. Opening the link uses the reader's own Jinear permissions, so it works "
                             + "for anyone who can already see the file, and for files shared with anyone who has the link. "
                             + "It does not return the file's contents.")
                .input(McpJsonSchema.object()
                        .requiredString("materialId", "File id, from list_files. Must be a FILE, not a FOLDER.")
                        .build())
                .output(McpSchemaGenerator.forType(McpFileLinkView.class))
                .readOnly()
                .scopes(OauthScope.FILES_READ)
                .handler((context, args) -> {
                    String materialId = args.requiredString("materialId");
                    McpFileLinkView view = new McpFileLinkView();
                    view.setMaterialId(materialId);
                    view.setUrl(oauthProperties.getIssuerUrl() + "/v1/material/media/" + materialId);
                    return McpToolResult.of(view);
                })
                .build();
    }

}
