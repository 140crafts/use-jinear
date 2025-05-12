package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "task_subscription")
public class TaskSubscription extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_subscription_id")
    private String taskSubscriptionId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "account_id")
    private String accountId;
}
