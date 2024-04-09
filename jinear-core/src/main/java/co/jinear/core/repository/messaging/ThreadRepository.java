package co.jinear.core.repository.messaging;

import co.jinear.core.model.entity.messaging.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface ThreadRepository extends JpaRepository<Thread, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Thread t
                set
                 t.lastActivityTime = :lastActivityTime,
                 t.lastUpdatedDate = current_timestamp()
                where
                     t.threadId = :threadId and
                     t.passiveId is null
                """)
    void updateLastActivityTime(@Param("threadId") String threadId,
                                @Param("lastActivityTime") ZonedDateTime lastActivityTime);

    Page<Thread> findAllByChannelIdAndPassiveIdIsNullOrderByLastActivityTimeDesc(String channelId, Pageable pageable);
}
