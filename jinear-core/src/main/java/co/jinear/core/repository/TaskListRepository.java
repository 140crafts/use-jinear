package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, String> {

    Optional<TaskList> findByTaskListIdAndPassiveIdIsNull(String taskListId);
}
