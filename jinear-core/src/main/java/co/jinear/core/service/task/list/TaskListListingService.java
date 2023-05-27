package co.jinear.core.service.task.list;

import co.jinear.core.converter.task.TaskListDtoConverter;
import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.repository.TaskListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListListingService {

    private static final int PAGE_SIZE = 15;

    private final TaskListRepository taskListRepository;
    private final TaskListDtoConverter taskListDtoConverter;

    public Page<TaskListDto> retrieveTaskLists(String workspaceId, String teamId, int page) {
        log.info("Retrieve all task lists from workspace and team has started. workspaceId: {}, teamId: {}, page: {}", workspaceId, teamId, page);
        return taskListRepository.findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, teamId, PageRequest.of(page, PAGE_SIZE))
                .map(taskListDtoConverter::convert);
    }
}
