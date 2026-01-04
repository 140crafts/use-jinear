package co.jinear.core.model.entity.material;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "material_access")
@FieldNameConstants
public class MaterialAccess extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "material_access_id")
    private String materialAccessId;

    @Column(name = "material_id")
    private String materialId;

    @Column(name = "account_id")
    private String accountId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "material_id", insertable = false, updatable = false)
    private Material material;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
