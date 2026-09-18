package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpFileLinkView implements McpToolPayload {

    @McpField("The file this link points at.")
    private String materialId;

    @McpField("Absolute Jinear URL that downloads the file.")
    private String url;
}
