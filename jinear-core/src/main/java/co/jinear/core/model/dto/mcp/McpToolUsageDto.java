package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McpToolUsageDto {

    private String toolName;
    private Long callCount;
    private Long errorCount;
    private Long averageDurationMs;
}
