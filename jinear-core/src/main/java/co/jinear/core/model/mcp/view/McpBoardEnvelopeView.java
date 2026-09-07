package co.jinear.core.model.mcp.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpBoardEnvelopeView implements McpToolPayload {

    private McpBoardView board;

    public static McpBoardEnvelopeView of(McpBoardView board) {
        McpBoardEnvelopeView envelope = new McpBoardEnvelopeView();
        envelope.setBoard(board);
        return envelope;
    }
}
