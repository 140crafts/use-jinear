package co.jinear.core.model.entity.oauth;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "oauth_refresh_token")
public class OauthRefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "oauth_refresh_token_id")
    private String oauthRefreshTokenId;

    @Column(name = "hashed_token")
    private String hashedToken;

    @Column(name = "oauth_connection_id")
    private String oauthConnectionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Date expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "consumed_at")
    private Date consumedAt;

    @Column(name = "rotated_to")
    private String rotatedTo;
}
