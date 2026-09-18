package co.jinear.core.manager.mcp.tool.task;

import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.task.TaskMediaManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.input.McpAddTaskAttachmentInput;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.mcp.view.McpTaskAttachmentAcknowledgementView;
import co.jinear.core.system.util.ByteArrayMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddTaskAttachmentTool implements McpTool {

    private static final String MULTIPART_FIELD = "file";
    private static final String TEXT_CONTENT_TYPE = "text/plain";
    private static final String BINARY_CONTENT_TYPE = "application/octet-stream";

    private final TaskMediaManager taskMediaManager;
    private final McpLinkConverter mcpLinkConverter;

    @Override
    public McpToolDefinition definition() {
        return McpToolDefinitionBuilder
                .named("add_task_attachment")
                .title("Attach a file to a task")
                .description("Attaches a new file to a task, attributed to the signed in account. Pass the content as text "
                             + "for text files such as Markdown, CSV or JSON, or as contentBase64 for binary files such as "
                             + "images or PDFs. Needs a PRO workspace with free storage. Returns the new attachment's id "
                             + "and a downloadUrl for the person.")
                .input(McpSchemaGenerator.forInput(McpAddTaskAttachmentInput.class))
                .output(McpSchemaGenerator.forType(McpTaskAttachmentAcknowledgementView.class))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .build();
    }

    @Override
    public McpToolResult call(McpToolContext context, McpToolArguments args) {
        McpAddTaskAttachmentInput input = args.bind(McpAddTaskAttachmentInput.class);
        boolean isText = Objects.nonNull(input.getText());
        if (isText == Objects.nonNull(input.getContentBase64())) {
            return McpToolResult.error("Pass exactly one of text or contentBase64.");
        }
        byte[] content;
        try {
            content = isText ? input.getText().getBytes(StandardCharsets.UTF_8) : decode(input.getContentBase64());
        } catch (IllegalArgumentException exception) {
            return McpToolResult.error("contentBase64 is not valid base64: " + exception.getMessage());
        }
        if (content.length == 0) {
            return McpToolResult.error("The file content is empty.");
        }
        MultipartFile file = new ByteArrayMultipartFile(MULTIPART_FIELD, input.getFileName(), contentType(input, isText), content);
        String mediaId = taskMediaManager.uploadTaskMedia(input.getTaskId(), file).getMediaId();
        String downloadUrl = mcpLinkConverter.taskAttachmentDownloadUrl(input.getTaskId(), mediaId);
        return McpToolResult.of(McpTaskAttachmentAcknowledgementView.of(input.getTaskId(), mediaId, input.getFileName(), (long) content.length, downloadUrl));
    }

    private byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64.replaceAll("\\s", ""));
    }

    private String contentType(McpAddTaskAttachmentInput input, boolean isText) {
        if (Objects.nonNull(input.getContentType()) && !input.getContentType().isBlank()) {
            return input.getContentType();
        }
        String guessed = URLConnection.guessContentTypeFromName(input.getFileName());
        if (Objects.nonNull(guessed)) {
            return guessed;
        }
        return isText ? TEXT_CONTENT_TYPE : BINARY_CONTENT_TYPE;
    }
}
