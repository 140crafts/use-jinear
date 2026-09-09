package co.jinear.core.manager.mcp.tool.file;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpFileLinkView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpGetFileLinkInput;

@Service
@RequiredArgsConstructor
public class GetFileLinkTool implements McpTool {

    private final OauthProperties oauthProperties;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("get_file_link")
                .title("Get a link to a file")
                .description("Returns the Jinear download link for a stored file, suitable for citing back to the person "
                             + "or for them to click. Opening the link uses the reader's own Jinear permissions, so it works "
                             + "for anyone who can already see the file, and for files shared with anyone who has the link. "
                             + "It does not return the file's contents.")
                .input(McpSchemaGenerator.forInput(McpGetFileLinkInput.class))
                .output(McpSchemaGenerator.forType(McpFileLinkView.class))
                .readOnly()
                .scopes(OauthScope.FILES_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpGetFileLinkInput input = args.bind(McpGetFileLinkInput.class);
        McpFileLinkView view = new McpFileLinkView();
        view.setMaterialId(input.getMaterialId());
        view.setUrl(oauthProperties.getIssuerUrl() + "/v1/material/media/" + input.getMaterialId());
        return McpToolResult.of(view);
    }
}
