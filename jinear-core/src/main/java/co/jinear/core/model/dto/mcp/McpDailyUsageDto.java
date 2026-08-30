package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McpDailyUsageDto {

    /** ISO date, yyyy-MM-dd. */
    private String date;
    private Long callCount;
    private Long errorCount;
}
