package co.jinear.core.model.mcp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * The behaviour hints a client uses to decide whether a call needs confirmation.
 * <p>
 * Both directories treat these as a review gate rather than a nicety: a tool with no
 * title, or with a hint that does not match what the tool actually does, is rejected.
 * readOnlyHint is what lets a client auto approve a call; destructiveHint is what makes
 * it always prompt.
 */
@Getter
@Builder
@ToString
public class McpToolAnnotations {

    private final String title;
    private final boolean readOnlyHint;
    private final boolean destructiveHint;
    private final boolean idempotentHint;
    /** False for every Jinear tool: they only touch this instance's own data. */
    @Builder.Default
    private final boolean openWorldHint = false;
}
