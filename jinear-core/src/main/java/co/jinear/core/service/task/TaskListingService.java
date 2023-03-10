package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromTeamVo;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromWorkspaceVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.repository.TaskSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskListingService {

    private static final int PAGE_SIZE = 50;

    private final TaskRepository taskRepository;
    private final TaskSearchRepository taskSearchRepository;
    private final TaskDtoConverter taskDtoConverter;

    public Page<TaskDto> retrieveAllTasksFromWorkspaceAndTeam(String workspaceId, String teamId, int page) {
        log.info("Retrieve all tasks from workspace and team has started. workspaceId: {}, teamId: {}, page: {}", workspaceId, teamId, page);
        return taskRepository.findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        workspaceId, teamId, PageRequest.of(page, PAGE_SIZE))
                .map(taskDtoConverter::map);
    }

    public List<TaskDto> retrieveAllIntersectingTasksFromTeam(SearchIntersectingTasksFromTeamVo searchIntersectingTasksFromTeamVo) {
        log.info("Find all intersecting tasks from team has started. searchIntersectingTasksVo: {}", searchIntersectingTasksFromTeamVo);
        return taskSearchRepository.findAllIntersectingTasksFromWorkspaceAndTeamBetween(searchIntersectingTasksFromTeamVo)
                .stream()
                .map(taskDtoConverter::map)
                .toList();
    }

    public List<TaskDto> retrieveAllIntersectingTasksFromTeamList(SearchIntersectingTasksFromWorkspaceVo searchIntersectingTasksFromWorkspaceVo) {
        log.info("Find all intersecting tasks from team has started. searchIntersectingTasksVo: {}", searchIntersectingTasksFromWorkspaceVo);
        return taskSearchRepository.findAllIntersectingTasksFromWorkspaceAndTeamListBetween(searchIntersectingTasksFromWorkspaceVo)
                .stream()
                .map(taskDtoConverter::map)
                .toList();
    }

    public Page<TaskDto> retrieveAllTasksFromWorkspaceAndTeamWithWorkflowStatus(String workspaceId, String teamId, String workflowStatusId, int page) {
        log.info("Retrieve all tasks from workspace and team with workflow status id has started. workspaceId: {}, teamId: {}, workflowStatusId: {}, page: {}", workspaceId, teamId, workflowStatusId, page);
        return taskRepository.findAllByWorkspaceIdAndTeamIdAndWorkflowStatusIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        workspaceId, teamId, workflowStatusId, PageRequest.of(page, PAGE_SIZE))
                .map(taskDtoConverter::map);
    }
}
