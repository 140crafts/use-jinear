package co.jinear.core.model.entity.mcp;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

/**
 * Daily rollup of mcp_tool_call_log, written before the raw log is pruned so the
 * usage charts keep a longer history than the 30 day log retention.
 */
@Getter
@Setter
@Entity
@Table(name = "mcp_usage_daily")
public class McpUsageDaily extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "mcp_usage_daily_id")
    private String mcpUsageDailyId;

    @Column(name = "usage_date")
    private LocalDate usageDate;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "tool_name")
    private String toolName;

    @Column(name = "call_count")
    private Long callCount;

    @Column(name = "error_count")
    private Long errorCount;

    @Column(name = "total_duration_ms")
    private Long totalDurationMs;
}
