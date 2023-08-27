package co.jinear.core.model.entity.payments;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "payment_settings")
public class PaymentSettings extends BaseEntity{

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "payment_settings_id")
    private String paymentSettingsId;

    @Column(name = "last_sync")
    private ZonedDateTime lastSync;
}
