package co.jinear.core.model.entity.account;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.username.Username;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "email")
    private String email;

    @Column(name = "email_confirmed")
    private Boolean emailConfirmed;

    @OneToMany(mappedBy = "account")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<AccountRole> roles;

    @OneToOne(mappedBy = "account")
    private Username username;

    @OneToOne(mappedBy = "account")
    private AccountPassword accountPassword;
}
