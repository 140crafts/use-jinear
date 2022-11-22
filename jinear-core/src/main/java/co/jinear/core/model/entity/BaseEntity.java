package co.jinear.core.model.entity;

import co.jinear.core.system.util.DateHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "idate", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "udate")
    private Date lastUpdatedDate;

    @Column(name = "passiveId")
    private String passiveId;

    @PrePersist
    void preInsert() {
        if (createdDate == null) {
            createdDate = DateHelper.now();
        }else{
            lastUpdatedDate = DateHelper.now();
        }
    }
}
