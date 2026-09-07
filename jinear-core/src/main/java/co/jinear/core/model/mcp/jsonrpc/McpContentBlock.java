package co.jinear.core.model.mcp.jsonrpc;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"type", "text"})
public class McpContentBlock {

    private static final String TYPE_TEXT = "text";

    private String type;
    private String text;

    public static McpContentBlock text(String text) {
        McpContentBlock block = new McpContentBlock();
        block.setType(TYPE_TEXT);
        block.setText(text);
        return block;
    }
}
