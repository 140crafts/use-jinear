package co.jinear.core.model.entity.management;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "instance_info")
public class InstanceInfo extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "instance_info_id")
    private String instanceInfoId;

    @Column(name = "telemetry_instance_id", nullable = false)
    private String telemetryInstanceId;

    @Column(name = "latest_known_version")
    private String latestKnownVersion;

    @Column(name = "last_check_date")
    private ZonedDateTime lastCheckDate;
}
