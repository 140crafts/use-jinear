package co.jinear.core.repository.reminder;

import co.jinear.core.model.entity.reminder.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, String> {

    Optional<Reminder> findByReminderIdAndPassiveIdIsNull(String reminderId);

    List<Reminder> findAllByOwnerIdAndPassiveIdIsNull(String ownerId);


    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Reminder r
                set r.passiveId = :passiveId
                    where
                        r.reminderId = :reminderId and
                        r.passiveId is null
                """)
    void updatePassiveId(@Param("reminderId") String reminderId,@Param("passiveId") String passiveId);
}
