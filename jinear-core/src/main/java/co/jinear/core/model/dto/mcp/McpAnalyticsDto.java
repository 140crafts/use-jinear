package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class McpAnalyticsDto {

    private Integer windowDays;
    private Long totalCalls;
    private Long errorCalls;
    private Long activeConnections;
    private List<McpToolUsageDto> topTools;
    private List<McpDailyUsageDto> daily;
}
