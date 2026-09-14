package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskAttachmentAcknowledgementView implements McpToolPayload {

    @McpField("The task the file was attached to.")
    private String taskId;

    @McpField("Id of the new attachment.")
    private String mediaId;

    @McpField("File name as stored.")
    private String name;

    @McpField("File size in bytes.")
    private Long size;

    @McpField("Absolute Jinear URL that downloads the file for a signed in reader who can see the task.")
    private String downloadUrl;

    @McpField("True when the operation completed.")
    private Boolean ok;

    public static McpTaskAttachmentAcknowledgementView of(String taskId, String mediaId, String name, Long size, String downloadUrl) {
        McpTaskAttachmentAcknowledgementView view = new McpTaskAttachmentAcknowledgementView();
        view.setTaskId(taskId);
        view.setMediaId(mediaId);
        view.setName(name);
        view.setSize(size);
        view.setDownloadUrl(downloadUrl);
        view.setOk(Boolean.TRUE);
        return view;
    }
}
