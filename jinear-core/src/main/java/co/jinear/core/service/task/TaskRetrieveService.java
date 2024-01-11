package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoDetailedConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskRetrieveService {

    private final TaskRepository taskRepository;
    private final TaskDtoDetailedConverter taskDtoDetailedConverter;

    public Task retrieveEntity(String taskId) {
        return taskRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .orElseThrow(NotFoundException::new);
    }

    public TaskDto retrievePlain(String taskId) {
        return taskRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .map(taskDtoDetailedConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public TaskDto retrieve(String taskId) {
        return taskRepository.findByTaskIdAndPassiveIdIsNull(taskId)
                .map(taskDtoDetailedConverter::mapAndRetrieveProfilePicturesAndTaskDetail)
                .orElseThrow(NotFoundException::new);
    }

    public TaskDto retrieve(String workspaceId, String teamId, Integer teamTagNo) {
        return taskRepository.findByWorkspaceIdAndTeamIdAndTeamTagNoAndPassiveIdIsNull(workspaceId, teamId, teamTagNo)
                .map(taskDtoDetailedConverter::mapAndRetrieveProfilePicturesAndTaskDetail)
                .orElseThrow(NotFoundException::new);
    }
}
