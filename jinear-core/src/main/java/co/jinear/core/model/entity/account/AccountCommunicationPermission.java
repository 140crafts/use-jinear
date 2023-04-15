package co.jinear.core.model.entity.account;

import co.jinear.core.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "account_communication_permission")
public class AccountCommunicationPermission extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "account_communication_permission_id")
    private String accountCommunicationPermissionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "email")
    private Boolean email;

    @Column(name = "push_notification")
    private Boolean pushNotification;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
