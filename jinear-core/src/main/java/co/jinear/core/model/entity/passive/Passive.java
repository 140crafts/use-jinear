package co.jinear.core.model.entity.passive;

import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.system.util.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "passive")
public class Passive {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "passive_id")
    private String passiveId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "idate", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "udate")
    private Date lastUpdatedDate;

    @Column(name = "responsible_account_id")
    private String responsibleAccountId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type")
    private PassiveReason reasonType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_subtype")
    private PassiveReason reasonSubtype;

    @PrePersist
    void preInsert() {
        if (createdDate == null) {
            createdDate = DateHelper.now();
        }else{
            lastUpdatedDate = DateHelper.now();
        }
        if (this.reason == null) {
            this.reason = PassiveReason.USER_ACTION.name();
        }
        if (this.reasonType == null) {
            this.reasonType = PassiveReason.USER_ACTION;
        }
        if (this.reasonSubtype == null) {
            this.reasonSubtype = PassiveReason.USER_ACTION;
        }
    }
}
