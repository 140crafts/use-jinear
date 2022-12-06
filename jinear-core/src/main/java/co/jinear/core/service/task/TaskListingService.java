package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.vo.task.SearchIntersectingTasksVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.repository.TaskSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskListingService {

    private static final int PAGE_SIZE = 500;

    private final TaskRepository taskRepository;
    private final TaskSearchRepository taskSearchRepository;
    private final ModelMapper modelMapper;

    public Page<TaskDto> retrieveAllTasksFromWorkspaceAndTeam(String workspaceId, String teamId, int page) {
        log.info("Retrieve all tasks from workspace and team has started. workspaceId: {}, teamId: {}, page: {}", workspaceId, teamId, page);
        return taskRepository.findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        workspaceId, teamId, PageRequest.of(page, PAGE_SIZE))
                .map(task -> modelMapper.map(task, TaskDto.class));
    }

    public List<TaskDto> retrieveAllIntersectingTasks(SearchIntersectingTasksVo searchIntersectingTasksVo) {
        log.info("Find all intersecting tasks has started. searchIntersectingTasksVo: {}", searchIntersectingTasksVo);
        return taskSearchRepository.findAllIntersectingTasksFromWorkspaceAndTeamBetween(searchIntersectingTasksVo).stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .toList();
    }
}
