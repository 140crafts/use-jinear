package co.jinear.core.service.workspace.activity;

import co.jinear.core.converter.workspace.WorkspaceActivityConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.vo.workspace.RetrieveWorkspaceActivityPageVo;
import co.jinear.core.repository.WorkspaceActivityRepository;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityRetrieveService {

    private static final int PAGE_SIZE = 25;
    private static final int TASK_ACTIVITIES_PAGE_SIZE = 125;

    private final WorkspaceActivityRepository workspaceActivityRepository;
    private final WorkspaceActivityConverter workspaceActivityConverter;
    private final WorkspaceActivityDetailRetrieveService workspaceActivityDetailRetrieveService;

    public WorkspaceActivityDto retrieveWorkspaceLatestActivity(String workspaceId, List<String> teamIdList) {
        log.info("Retrieve workspace latest activity has started. workspaceId: {}, teamIdList: {}", workspaceId, NormalizeHelper.listToString(teamIdList));
        return workspaceActivityRepository.findFirstByWorkspaceIdAndTeamIdIsInAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, teamIdList)
                .map(workspaceActivityConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Page<WorkspaceActivityDto> retrieveWorkspaceActivities(RetrieveWorkspaceActivityPageVo retrieveWorkspaceActivityPageVo) {
        log.info("Retrieve workspace activities has started. retrieveWorkspaceActivityPageVo: {}", retrieveWorkspaceActivityPageVo);
        PageRequest pageRequest = Optional.of(retrieveWorkspaceActivityPageVo)
                .map(RetrieveWorkspaceActivityPageVo::getPage)
                .map(page -> PageRequest.of(page, PAGE_SIZE))
                .orElse(PageRequest.of(0, PAGE_SIZE));
        return workspaceActivityRepository.findAllByWorkspaceIdAndTeamIdIsInAndPassiveIdIsNullOrderByCreatedDateDesc(retrieveWorkspaceActivityPageVo.getWorkspaceId(), retrieveWorkspaceActivityPageVo.getTeamIdList(), pageRequest)
                .map(workspaceActivityDetailRetrieveService::retrieveDetailsAndMap);
    }

    public Page<WorkspaceActivityDto> retrieveTaskActivities(String taskId, int page) {
        log.info("Retrieve task activities has started. taskId: {}, page: {}", taskId, page);
        PageRequest pageRequest = PageRequest.of(page, TASK_ACTIVITIES_PAGE_SIZE);
        return workspaceActivityRepository.findAllByTaskIdAndPassiveIdIsNullOrderByCreatedDateDesc(taskId, pageRequest)
                .map(workspaceActivityDetailRetrieveService::retrieveDetailsAndMap);
    }

}
