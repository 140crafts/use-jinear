package co.jinear.core.model.entity.mcp;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * Rotating refresh token. OAuth 2.1 requires rotation for public clients, and both
 * DCR and CIMD register Claude as a public client.
 * <p>
 * consumed_at plus rotated_to give reuse detection: presenting a token that already
 * has a successor means the token leaked, so the whole connection is revoked.
 */
@Getter
@Setter
@Entity
@Table(name = "mcp_refresh_token")
public class McpRefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "mcp_refresh_token_id")
    private String mcpRefreshTokenId;

    @Column(name = "hashed_token")
    private String hashedToken;

    @Column(name = "mcp_connection_id")
    private String mcpConnectionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Date expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "consumed_at")
    private Date consumedAt;

    @Column(name = "rotated_to")
    private String rotatedTo;
}
