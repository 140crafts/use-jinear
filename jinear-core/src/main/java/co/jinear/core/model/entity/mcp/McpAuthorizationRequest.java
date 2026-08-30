package co.jinear.core.model.entity.mcp;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * A validated /authorize call parked while the user decides on the consent screen.
 * The row id is the request_id handed to the frontend, so nothing about the client
 * or the redirect target is carried in a query string the user could tamper with.
 */
@Getter
@Setter
@Entity
@Table(name = "mcp_authorization_request")
public class McpAuthorizationRequest extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "mcp_authorization_request_id")
    private String mcpAuthorizationRequestId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @Column(name = "scope")
    private String scope;

    @Column(name = "state")
    private String state;

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
    @Column(name = "completed_at")
    private Date completedAt;
}
