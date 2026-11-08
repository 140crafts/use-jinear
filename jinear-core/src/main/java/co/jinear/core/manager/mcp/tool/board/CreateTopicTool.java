package co.jinear.core.manager.mcp.tool.board;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.topic.TopicManager;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpCreateTopicInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTopicEnvelopeView;
import co.jinear.core.model.mcp.view.McpTopicView;
import co.jinear.core.model.request.topic.TopicInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTopicTool implements McpTool {

    private final TopicManager topicManager;
    private final McpViewConverter mcpViewConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("create_topic")
                .title("Create a topic")
                .description("Creates a topic in a team, the label create_task and update_task take as topicId. Returns the topic with its id.")
                .input(McpSchemaGenerator.forInput(McpCreateTopicInput.class))
                .output(McpSchemaGenerator.single("topic", "The created topic.", McpTopicView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpCreateTopicInput input = args.bind(McpCreateTopicInput.class);
        context.setWorkspaceId(input.getWorkspaceId());
        TopicInitializeRequest request = new TopicInitializeRequest();
        request.setWorkspaceId(input.getWorkspaceId());
        request.setTeamId(input.getTeamId());
        request.setName(input.getName());
        request.setTag(input.getTag());
        request.setColor(input.getColor());
        TopicDto topic = topicManager.initializeTopic(request).getTopicDto();
        return McpToolResult.of(McpTopicEnvelopeView.of(mcpViewConverter.topic(topic)));
    }
}
