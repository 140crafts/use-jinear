package co.jinear.core.repository.task;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.entity.task.TaskCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskCollaboratorRepository extends JpaRepository<TaskCollaborator, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update TaskCollaborator taskCollaborator
                set taskCollaborator.passiveId=:passiveId
                    where
                        taskCollaborator.taskId = :taskId and
                        taskCollaborator.passiveId is null
            """)
    void updateAllPassive(@Param("taskId") String taskId, @Param("passiveId") String passiveId);

    String task(Task task);
}
