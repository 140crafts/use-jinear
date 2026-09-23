package co.jinear.core.model.mcp.input;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpAddTaskAttachmentInput {

    @NotBlank
    @McpField(McpInputDescriptions.TASK_ID)
    private String taskId;

    @NotBlank
    @McpField("File name with its extension, for example notes.md or chart.png.")
    private String fileName;

    @McpField("MIME type, for example text/markdown or image/png. Guessed from fileName when omitted.")
    private String contentType;

    @McpField("File content as plain text, stored as UTF-8. For text files. Pass either text or contentBase64, not both.")
    private String text;

    @McpField("File content encoded as base64. For binary files such as images or PDFs. Pass either text or contentBase64, not both.")
    private String contentBase64;
}
