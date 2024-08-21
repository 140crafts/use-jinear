package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoDetailedConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.repository.TaskSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingService {

    private final TaskSearchRepository taskSearchRepository;
    private final TaskDtoDetailedConverter taskDtoDetailedConverter;
    private final TaskRepository taskRepository;

    public Page<TaskDto> filterTasks(TaskSearchFilterVo taskSearchFilterVo) {
        log.info("Filter tasks has started. taskSearchFilterVo: {}", taskSearchFilterVo);
        return taskSearchRepository.filterBy(taskSearchFilterVo)
                .map(taskDtoDetailedConverter::map);
    }

    public boolean checkAnyActiveTaskExistsWithTeamAndProject(String teamId, String projectId) {
        log.info("Check any active task exists with team and project has started. teamId: {}, projectId: {}", teamId, projectId);
        boolean exist = taskRepository.existsByTeamIdAndProjectIdAndPassiveIdIsNull(teamId, projectId);
        log.info("Check any active task exists with team and project has completed. teamId: {}, projectId: {}, exist: {}", teamId, projectId, exist);
        return exist;
    }
}
