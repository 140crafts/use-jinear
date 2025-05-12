package co.jinear.core.model.entity.google;

import co.jinear.core.converter.EncryptedColumnConverter;
import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "google_token")
public class GoogleToken extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "google_token_id")
    private String googleTokenId;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name = "access_token")
    private String accessToken;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name = "refresh_token")
    private String refreshToken;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name = "id_token")
    private String idToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "google_user_info_id")
    private String googleUserInfoId;

    @Column(name = "last_mail_check")
    private ZonedDateTime lastMailCheck;

    @OneToMany(mappedBy = "googleToken")
    @Where(clause = "passive_id is null")
    private Set<GoogleTokenScope> scopes;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "google_user_info_id", insertable = false, updatable = false)
    private GoogleUserInfo googleUserInfo;
}
