package co.jinear.core.model.entity.management;

import co.jinear.core.converter.management.InstanceFlagTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "instance_flag")
@FieldNameConstants
public class InstanceFlag extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "instance_flag_id")
    private String instanceFlagId;

    @Convert(converter = InstanceFlagTypeConverter.class)
    @Column(name = "flag_type", nullable = false)
    private InstanceFlagType flagType;

    @Column(name = "flag_value", columnDefinition = "text")
    private String flagValue;
}
