package co.jinear.core.model.entity.google;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "google_user_info")
public class GoogleUserInfo extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "google_user_info_id")
    private String googleUserInfoId;

    @Column(name = "sub")
    private String sub;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private String emailVerified;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "locale")
    private String locale;
}
