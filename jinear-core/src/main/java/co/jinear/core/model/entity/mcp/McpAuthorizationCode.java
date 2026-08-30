package co.jinear.core.model.entity.mcp;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * Single use authorization code. The code handed to the client is
 * "{mcp_authorization_code_id}.{secret}", so we can look the row up by id and then
 * BCrypt match the secret, the same shape the robot token uses.
 */
@Getter
@Setter
@Entity
@Table(name = "mcp_authorization_code")
public class McpAuthorizationCode extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "mcp_authorization_code_id")
    private String mcpAuthorizationCodeId;

    @Column(name = "hashed_code")
    private String hashedCode;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "mcp_connection_id")
    private String mcpConnectionId;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @Column(name = "scope")
    private String scope;

    @Column(name = "code_challenge")
    private String codeChallenge;

    @Column(name = "code_challenge_method")
    private String codeChallengeMethod;

    @Column(name = "resource")
    private String resource;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Date expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "consumed_at")
    private Date consumedAt;
}
