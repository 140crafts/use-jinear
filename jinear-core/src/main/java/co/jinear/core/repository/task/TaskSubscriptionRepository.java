package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.TaskSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskSubscriptionRepository extends JpaRepository<TaskSubscription, String> {

    Optional<TaskSubscription> findByTaskSubscriptionIdAndPassiveIdIsNull(String taskSubscriptionId);

    Optional<TaskSubscription> findByAccountIdAndTaskIdAndPassiveIdIsNull(String accountId, String taskId);

    long countAllByAccountIdAndTaskIdAndPassiveIdIsNull(String accountId, String taskId);

    List<TaskSubscription> findAllByTaskIdAndPassiveIdIsNull(String taskId);
}
