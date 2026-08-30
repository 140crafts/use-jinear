package co.jinear.core.model.entity.mcp;

import co.jinear.core.converter.mcp.McpToolCallStatusConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.mcp.McpToolCallStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * One row per tools/call.
 * <p>
 * Deliberately holds no arguments and no response payload. Tool arguments are
 * derived from the user's conversation, and both directory policies require a
 * connector to collect only what its function needs. Sizes and timings are enough
 * to answer "what is this connection doing" without storing anyone's content.
 */
@Getter
@Setter
@Entity
@Table(name = "mcp_tool_call_log")
public class McpToolCallLog extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "mcp_tool_call_log_id")
    private String mcpToolCallLogId;

    @Column(name = "mcp_connection_id")
    private String mcpConnectionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "tool_name")
    private String toolName;

    @Convert(converter = McpToolCallStatusConverter.class)
    @Column(name = "call_status")
    private McpToolCallStatus callStatus;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "response_bytes")
    private Integer responseBytes;
}
