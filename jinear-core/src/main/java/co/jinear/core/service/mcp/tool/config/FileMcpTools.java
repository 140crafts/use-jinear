package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.model.enumtype.mcp.McpScope;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * File browsing.
 * <p>
 * Read only. Uploading a file needs multipart or a presigned URL round trip, neither of
 * which maps onto a tool call, so files are listed and linked rather than transferred.
 */
@Configuration
@RequiredArgsConstructor
public class FileMcpTools {

    private final MaterialListingManager materialListingManager;
    private final McpProperties mcpProperties;

    @Bean
    public McpTool listFilesTool() {
        return SimpleMcpTool.named("list_files")
                .title("List files and folders")
                .description("Lists the contents of a workspace's file storage, one folder at a time. "
                        + "Omit parentFolderId to list the root. Returns both folders and files; "
                        + "pass a folder's materialId back as parentFolderId to descend into it.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .string("parentFolderId", "Folder to list. Omit for the root of the workspace.")
                        .enumeration("type", "Return only FOLDER entries or only FILE entries. Omit for both.",
                                List.of("FOLDER", "FILE"), false)
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpShapes.pageSchema("Files and folders in this location.", McpShapes.fileSchema()))
                .readOnly()
                .scopes(McpScope.FILES_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    MaterialSearchRequest request = new MaterialSearchRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setParentMaterialId(args.optionalString("parentFolderId", null));
                    request.setPage(args.page());
                    String type = args.optionalString("type", null);
                    if (Objects.nonNull(type)) {
                        request.setMaterialType(parseType(type));
                    }
                    context.setWorkspaceId(request.getWorkspaceId());
                    var hierarchy = materialListingManager.search(request).getMaterialHierarchyDto();
                    return McpToolResult.of(McpShapes.page(hierarchy.getContent(), McpShapes::file));
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
                .output(McpJsonSchema.object()
                        .string("materialId", "The file this link points at.")
                        .string("url", "Absolute Jinear URL that downloads the file.")
                        .build())
                .readOnly()
                .scopes(McpScope.FILES_READ)
                .handler((context, arguments) -> {
                    String materialId = McpToolArguments.of(arguments).requiredString("materialId");
                    var node = McpShapes.object();
                    node.put("materialId", materialId);
                    node.put("url", mcpProperties.getIssuerUrl() + "/v1/material/media/" + materialId);
                    return McpToolResult.of(node);
                })
                .build();
    }

    private MaterialType parseType(String value) {
        try {
            return MaterialType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new McpToolException("invalid_argument", "type must be FOLDER or FILE. Received: " + value);
        }
    }
}
