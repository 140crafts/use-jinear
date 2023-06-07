package co.jinear.core.service.task.board;

import co.jinear.core.converter.task.TaskBoardDtoConverter;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.repository.TaskBoardRepository;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardListingService {

    private static final int PAGE_SIZE = 15;
    private static final int SEARCH_PAGE_SIZE = 75;

    private final TaskBoardRepository taskBoardRepository;
    private final TaskBoardDtoConverter taskBoardDtoConverter;

    public Page<TaskBoardDto> retrieveTaskBoards(String workspaceId, String teamId, int page) {
        log.info("Retrieve all task boards from workspace and team has started. workspaceId: {}, teamId: {}, page: {}", workspaceId, teamId, page);
        return taskBoardRepository.findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, teamId, PageRequest.of(page, PAGE_SIZE))
                .map(taskBoardDtoConverter::convert);
    }

    public Page<TaskBoardDto> retrieveTaskBoardsExcludingSome(List<String> excludingBoardIds, String workspaceId, String teamId, int page) {
        log.info("Retrieve all task boards with some exclusion from workspace and team has started. workspaceId: {}, teamId: {}, page: {}, excludingBoardIds: [{}]", workspaceId, teamId, page, NormalizeHelper.listToString(excludingBoardIds));
        return taskBoardRepository.findAllByTaskBoardIdNotInAndWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(excludingBoardIds, workspaceId, teamId, PageRequest.of(page, SEARCH_PAGE_SIZE))
                .map(taskBoardDtoConverter::convert);
    }

    public Page<TaskBoardDto> retrieveTaskBoardsExcludingSomeAndFilterByName(List<String> excludingBoardIds, String workspaceId, String teamId, String filterRecentsByName, int page) {
        log.info("Retrieve all task boards with some exclusion from workspace and team has started. workspaceId: {}, teamId: {}, filterRecentsByName: {}, page: {}, excludingBoardIds: [{}]", workspaceId, teamId, page, NormalizeHelper.listToString(excludingBoardIds));
        String filterLike = "%" + filterRecentsByName + "%";
        return taskBoardRepository.findAllByTaskBoardIdNotInAndWorkspaceIdAndTeamIdAndTitleLikeIgnoreCaseAndPassiveIdIsNullOrderByCreatedDateDesc(excludingBoardIds, workspaceId, teamId, filterLike, PageRequest.of(page, SEARCH_PAGE_SIZE))
                .map(taskBoardDtoConverter::convert);
    }
}
