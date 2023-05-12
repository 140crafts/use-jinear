package co.jinear.core.manager.workspace;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.response.workspace.WorkspaceActivityListResponse;
import co.jinear.core.model.vo.workspace.RetrieveWorkspaceActivityPageVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.activity.WorkspaceActivityRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceActivityRetrieveService workspaceActivityRetrieveService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskAccessValidator taskAccessValidator;

    public WorkspaceActivityListResponse retrieveWorkspaceActivities(String workspaceId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve workspace activities has started. currentAccountId: {}", currentAccountId);
        List<String> accountTeamIds = teamMemberRetrieveService.retrieveAllTeamIdsOfAnAccount(currentAccountId, workspaceId);
        RetrieveWorkspaceActivityPageVo vo = mapRetrieveWorkspaceActivityPageVo(workspaceId, page, accountTeamIds);
        Page<WorkspaceActivityDto> workspaceActivityDtoPage = workspaceActivityRetrieveService.retrieveWorkspaceActivities(vo);
        return mapResponse(workspaceActivityDtoPage);
    }

    public WorkspaceActivityListResponse retrieveWorkspaceTeamActivities(String workspaceId, String teamId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve workspace activities has started. currentAccountId: {}", currentAccountId);
        RetrieveWorkspaceActivityPageVo vo = mapRetrieveWorkspaceActivityPageVo(workspaceId, page, List.of(teamId));
        Page<WorkspaceActivityDto> workspaceActivityDtoPage = workspaceActivityRetrieveService.retrieveWorkspaceActivities(vo);
        return mapResponse(workspaceActivityDtoPage);
    }

    public WorkspaceActivityListResponse retrieveTaskActivities(String taskId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        Page<WorkspaceActivityDto> workspaceActivityDtoPage = workspaceActivityRetrieveService.retrieveTaskActivities(taskId, page);
        return mapResponse(workspaceActivityDtoPage);
    }

    private RetrieveWorkspaceActivityPageVo mapRetrieveWorkspaceActivityPageVo(String workspaceId, int page, List<String> accountTeamIds) {
        RetrieveWorkspaceActivityPageVo vo = new RetrieveWorkspaceActivityPageVo();
        vo.setWorkspaceId(workspaceId);
        vo.setTeamIdList(accountTeamIds);
        vo.setPage(page);
        return vo;
    }

    private WorkspaceActivityListResponse mapResponse(Page<WorkspaceActivityDto> workspaceActivityDtoPage) {
        WorkspaceActivityListResponse workspaceActivityListResponse = new WorkspaceActivityListResponse();
        workspaceActivityListResponse.setWorkspaceActivityDtoPageDto(new PageDto<>(workspaceActivityDtoPage));
        return workspaceActivityListResponse;
    }
}
