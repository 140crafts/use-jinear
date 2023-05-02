package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChecklistRepository extends JpaRepository<Checklist, String> {

    Optional<Checklist> findByChecklistId(String checklistId);

    Optional<Checklist> findByChecklistIdAndPassiveIdIsNull(String checklistId);

    long countAllByTaskIdAndPassiveIdIsNull(String taskId);
}
