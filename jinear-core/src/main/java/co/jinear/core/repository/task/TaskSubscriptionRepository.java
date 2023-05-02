package co.jinear.core.repository.task;

import co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto;
import co.jinear.core.model.entity.task.TaskSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskSubscriptionRepository extends JpaRepository<TaskSubscription, String> {

    Optional<TaskSubscription> findByTaskSubscriptionIdAndPassiveIdIsNull(String taskSubscriptionId);

    Optional<TaskSubscription> findByAccountIdAndTaskIdAndPassiveIdIsNull(String accountId, String taskId);

    long countAllByAccountIdAndTaskIdAndPassiveIdIsNull(String accountId, String taskId);

    List<TaskSubscription> findAllByTaskIdAndPassiveIdIsNull(String taskId);

    @Query("""
            select new co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto(
                acc.accountId,
                acc.email,
                acc.localeType,
                acc.timeZone,
                acp.email,
                acp.pushNotification)
                    from TaskSubscription  ts, Account acc, AccountCommunicationPermission  acp 
                        where 
                            ts.taskId=:taskId and
                            ts.accountId=acc.accountId and
                            ts.accountId = acp.accountId and
                            ts.passiveId is null and
                            acc.passiveId is null and 
                            acp.passiveId is null
            """)
    List<TaskSubscriptionWithCommunicationPreferencesDto> retrieveSubscribersWithCommunicationInfo(@Param("taskId") String taskId);

}
