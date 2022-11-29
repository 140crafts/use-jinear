package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskListingService {

    private static final int PAGE_SIZE = 250;

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public Page<TaskDto> retrieveAllTasksFromWorkspaceAndTeam(String workspaceId, String teamId, int page) {
        log.info("Retrieve all tasks from workspace and team has started. workspaceId: {}, teamId: {}, page: {}", workspaceId, teamId, page);
        return taskRepository.findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        workspaceId, teamId, PageRequest.of(page, PAGE_SIZE))
                .map(task -> modelMapper.map(task, TaskDto.class));
    }

}
