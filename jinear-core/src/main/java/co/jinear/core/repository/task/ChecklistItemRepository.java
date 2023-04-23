package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, String> {

    Optional<ChecklistItem> findByChecklistItemIdAndPassiveIdIsNull(String checklistItemId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ChecklistItem  checklistItem
                set checklistItem.isChecked = :checked
                    where 
                        checklistItem.checklistItemId = :checklistItemId and 
                        checklistItem.passiveId is null
                """)
    void updateChecked(@Param("checklistItemId") String checklistItemId, @Param("checked") boolean checked);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ChecklistItem  checklistItem
                set checklistItem.label = :label
                    where 
                        checklistItem.checklistItemId = :checklistItemId and 
                        checklistItem.passiveId is null
                """)
    void updateLabel(@Param("checklistItemId") String checklistItemId, @Param("label") String label);
}
