package co.jinear.core.model.entity.account;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "apple_user")
public class AppleUser extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "apple_user_id")
    private String appleUserId;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "external_apple_id", nullable = false)
    private String externalAppleId;

    @Column(name = "apple_mail")
    private String appleMail;

    @Column(name = "apple_name")
    private String appleName;
}
