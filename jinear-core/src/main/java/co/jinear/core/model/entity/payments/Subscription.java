package co.jinear.core.model.entity.payments;

import co.jinear.core.converter.payments.SubscriptionStatusTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "payments_service_subscription_id")
    private String paymentsServiceSubscriptionId;

    @Convert(converter = SubscriptionStatusTypeConverter.class)
    @Column(name = "status")
    private SubscriptionStatus subscriptionStatus;

    @Column(name = "related_object_id")
    private String relatedObjectId;
}
