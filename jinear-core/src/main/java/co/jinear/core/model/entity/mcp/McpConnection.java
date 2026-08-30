package co.jinear.core.model.entity.mcp;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * A standing grant from one account to one OAuth client.
 * <p>
 * Connections are account level rather than workspace level, mirroring how the app
 * works: a tool call reaches whatever workspaces the account is a member of, and
 * the existing manager layer enforces that per call. Revoking sets passive_id, which
 * makes every outstanding access token fail its connection lookup on the next call.
 */
@Getter
@Setter
@Entity
@Table(name = "mcp_connection")
public class McpConnection extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "mcp_connection_id")
    private String mcpConnectionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    /** Space separated, in the order the discovery document lists them. */
    @Column(name = "granted_scopes")
    private String grantedScopes;

    /**
     * The session_info row this connection acts under. Every write manager records the
     * session id on its activity row, so a connection needs one just like a browser
     * login does.
     */
    @Column(name = "session_info_id")
    private String sessionInfoId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_used_at")
    private Date lastUsedAt;
}
