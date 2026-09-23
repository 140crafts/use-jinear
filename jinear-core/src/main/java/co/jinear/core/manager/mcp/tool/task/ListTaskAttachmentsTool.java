package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskMediaManager;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpListTaskAttachmentsInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpTaskAttachmentView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTaskAttachmentsTool implements McpTool {

    private final TaskMediaManager taskMediaManager;
    private final McpViewConverter mcpViewConverter;
    private final McpLinkConverter mcpLinkConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("list_task_attachments")
                .title("List a task's attachments")
                .description("Lists the files attached to a task, oldest first. Each item carries a downloadUrl for the person "
                             + "to click. Opening it uses the reader's own Jinear session, so it works in a browser signed in "
                             + "to an account that can see the task. It does not return the files' contents.")
                .input(McpSchemaGenerator.forInput(McpListTaskAttachmentsInput.class))
                .output(McpSchemaGenerator.list(McpTaskAttachmentView.class, "Files attached to this task."))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpListTaskAttachmentsInput input = args.bind(McpListTaskAttachmentsInput.class);
        List<MediaDto> attachments = taskMediaManager.retrieveTaskMediaList(input.getTaskId()).getData();
        return McpToolResult.of(McpListView.of(attachments, media -> mcpViewConverter.taskAttachment(
                media, mcpLinkConverter.taskAttachmentDownloadUrl(input.getTaskId(), media.getMediaId()))));
    }
}
