package co.jinear.core.service.task.board;

import co.jinear.core.converter.task.TaskBoardDtoConverter;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.repository.TaskBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardListingService {

    private static final int PAGE_SIZE = 15;

    private final TaskBoardRepository taskBoardRepository;
    private final TaskBoardDtoConverter taskBoardDtoConverter;

    public Page<TaskBoardDto> retrieveTaskBoards(String workspaceId, String teamId, int page) {
        log.info("Retrieve all task boards from workspace and team has started. workspaceId: {}, teamId: {}, page: {}", workspaceId, teamId, page);
        return taskBoardRepository.findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, teamId, PageRequest.of(page, PAGE_SIZE))
                .map(taskBoardDtoConverter::convert);
    }
}
