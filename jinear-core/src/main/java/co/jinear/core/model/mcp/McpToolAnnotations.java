package co.jinear.core.model.mcp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class McpToolAnnotations {

    private final String title;
    private final boolean readOnlyHint;
    private final boolean destructiveHint;
    private final boolean idempotentHint;
    @Builder.Default
    private final boolean openWorldHint = false;
}
