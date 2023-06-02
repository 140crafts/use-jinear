package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskBoardEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskBoardEntryRepository extends JpaRepository<TaskBoardEntry, String> {

    Optional<TaskBoardEntry> findByTaskBoardEntryIdAndPassiveIdIsNull(String taskBoardEntryId);

    Optional<TaskBoardEntry> findByTaskIdAndTaskBoardIdAndPassiveIdIsNull(String taskId, String taskBoardId);

    Page<TaskBoardEntry> findAllByTaskBoardIdAndPassiveIdIsNullOrderByOrder(String taskListId, Pageable pageable);

    Long countAllByTaskBoardIdAndPassiveIdIsNull(String taskListId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update TaskBoardEntry taskBoardEntry
                set taskBoardEntry.order = taskBoardEntry.order + 1
                    where 
                        taskBoardEntry.taskBoardId = :taskBoardId and 
                        (:newOrder <= taskBoardEntry.order and taskBoardEntry.order < :currentOrder) and  
                        taskBoardEntry.passiveId is null
                """)
    void updateOrderUpward(@Param("taskBoardId") String taskBoardId,
                           @Param("currentOrder") int currentOrder,
                           @Param("newOrder") int newOrder);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update TaskBoardEntry taskBoardEntry
                set taskBoardEntry.order = taskBoardEntry.order - 1
                    where 
                        taskBoardEntry.taskBoardId = :taskBoardId and 
                        (:currentOrder < taskBoardEntry.order and :newOrder <= taskBoardEntry.order) and  
                        taskBoardEntry.passiveId is null
                """)
    void updateOrderDownward(@Param("taskBoardId") String taskBoardId,
                             @Param("currentOrder") int currentOrder,
                             @Param("newOrder") int newOrder);

}
