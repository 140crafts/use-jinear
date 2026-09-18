package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskAttachmentView implements McpToolPayload {

    @McpField("Attachment id.")
    private String mediaId;

    @McpField("Task this file is attached to.")
    private String taskId;

    @McpField("File name as uploaded.")
    private String name;

    @McpField("MIME type, or null when unknown.")
    private String contentType;

    @McpField("File size in bytes.")
    private Long size;

    @McpField("Visibility of the stored file.")
    private String visibility;

    @McpField("ISO 8601 instant the file was attached.")
    private String createdAt;

    @McpField("Absolute Jinear URL that downloads the file for a signed in reader who can see the task.")
    private String downloadUrl;
}
