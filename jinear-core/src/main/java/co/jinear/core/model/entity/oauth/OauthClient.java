package co.jinear.core.model.entity.oauth;

import co.jinear.core.converter.oauth.OauthClientRegistrationTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.oauth.OauthClientRegistrationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "oauth_client")
public class OauthClient extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "oauth_client_id")
    private String oauthClientId;

    /** For DCR and STATIC this is a ULID. For CIMD it is the https metadata document URL. */
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_uri")
    private String clientUri;

    @Column(name = "logo_uri")
    private String logoUri;

    @Column(name = "policy_uri")
    private String policyUri;

    @Column(name = "tos_uri")
    private String tosUri;

    /** Newline separated. Loopback entries are matched with the port ignored. */
    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "grant_types")
    private String grantTypes;

    @Column(name = "token_endpoint_auth_method")
    private String tokenEndpointAuthMethod;

    @Convert(converter = OauthClientRegistrationTypeConverter.class)
    @Column(name = "registration_type")
    private OauthClientRegistrationType registrationType;

    @Column(name = "software_id")
    private String softwareId;

    @Column(name = "software_version")
    private String softwareVersion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "client_id_issued_at")
    private Date clientIdIssuedAt;
}
