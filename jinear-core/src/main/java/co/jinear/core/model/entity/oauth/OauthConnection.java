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
@Table(name = "oauth_connection")
public class OauthConnection extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "oauth_connection_id")
    private String oauthConnectionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "granted_scopes")
    private String grantedScopes;

    @Column(name = "session_info_id")
    private String sessionInfoId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_used_at")
    private Date lastUsedAt;
}
