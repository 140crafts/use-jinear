package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.mcp.view.McpTopicView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.jinear.core.model.mcp.input.McpListTopicsInput;

@Service
@RequiredArgsConstructor
public class ListTopicsTool implements McpTool {

    private final TopicListingManager topicListingManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_topics")
                .title("List a team's topics")
                .description("Lists the topics a team uses to label its tasks. "
                             + "Read this to turn a label a person named into the topicId create_task takes.")
                .input(McpSchemaGenerator.forInput(McpListTopicsInput.class))
                .output(McpSchemaGenerator.page(McpTopicView.class, "Topics in this team."))
                .readOnly()
                .scopes(OauthScope.WORKSPACE_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTopicsInput input = args.bind(McpListTopicsInput.class);
        PageDto<TopicDto> page = topicListingManager
                .retrieveTeamTopics(input.getTeamId(), args.page())
                .getTopicDtoPage();
        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::topic));
    }
}
