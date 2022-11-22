package co.jinear.core.model.entity.account;


import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.account.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "account_role")
public class AccountRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "account_role_id")
    private Long accountRoleId;

    @Column(name = "account_id")
    private String accountId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
