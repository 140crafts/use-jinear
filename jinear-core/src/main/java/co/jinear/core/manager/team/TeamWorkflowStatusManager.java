package co.jinear.core.manager.team;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.request.team.InitializeTeamWorkflowStatusRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.team.TeamWorkflowStatusListingResponse;
import co.jinear.core.model.vo.team.workflow.InitializeTeamWorkflowStatusVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamWorkflowStatusManager {

    private final TeamWorkflowStatusRetrieveService teamWorkflowStatusRetrieveService;
    private final TeamWorkflowStatusService teamWorkflowStatusService;
    private final TeamRetrieveService teamRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final TeamAccessValidator teamAccessValidator;

    public TeamWorkflowStatusListingResponse retrieveAllFromTeam(String teamId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Retrieve all team workflow statuses from team has started. currentAccountId: {}", currentAccountId);
        List<TeamWorkflowStatusDto> teamWorkflowStatusDtos = teamWorkflowStatusRetrieveService.retrieveAll(teamId);
        return mapResponse(teamWorkflowStatusDtos);
    }

    public BaseResponse initializeTeamWorkflowStatus(String teamId, InitializeTeamWorkflowStatusRequest initializeTeamWorkflowStatusRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        log.info("Initialize team workflow status has started. currentAccountId: {}", currentAccountId);
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        InitializeTeamWorkflowStatusVo initializeTeamWorkflowStatusVo = mapRequestToInitializeVo(initializeTeamWorkflowStatusRequest, teamDto);
        teamWorkflowStatusService.initializeTeamWorkflowStatus(initializeTeamWorkflowStatusVo);
        return new BaseResponse();
    }

    public BaseResponse removeTeamWorkflowStatus(String teamId, String teamWorkflowStatusId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        validateTeamIdAndTeamWorkflowStatusTeamIdMatch(teamId, teamWorkflowStatusId);
        log.info("Remove team workflow status has started. currentAccountId: {}", currentAccountId);
        teamWorkflowStatusService.removeTeamWorkflowStatus(teamWorkflowStatusId, currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse changeOrder(String teamId, String teamWorkflowStatusId, String replaceWithTeamWorkflowStatusId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
        validateTeamWorkflowStatusesInSameTeamAndBothAreInGivenTeam(teamId, teamWorkflowStatusId, replaceWithTeamWorkflowStatusId);
        log.info("Change team workflow status order has started. currentAccountId: {}", currentAccountId);
        teamWorkflowStatusService.changeTeamWorkflowStatusOrder(teamWorkflowStatusId, replaceWithTeamWorkflowStatusId);
        return new BaseResponse();
    }

    private void validateTeamIdAndTeamWorkflowStatusTeamIdMatch(String teamId, String teamWorkflowStatusId) {
        TeamWorkflowStatusDto teamWorkflowStatusDto = teamWorkflowStatusRetrieveService.retrieve(teamWorkflowStatusId);
        if (!teamWorkflowStatusDto.getTeamId().equalsIgnoreCase(teamId)) {
            throw new NoAccessException();
        }
    }

    private void validateTeamWorkflowStatusesInSameTeamAndBothAreInGivenTeam(String teamId, String teamWorkflowStatusId, String replaceWithTeamWorkflowStatusId) {
        TeamWorkflowStatusDto teamWorkflowStatusDto = teamWorkflowStatusRetrieveService.retrieve(teamWorkflowStatusId);
        TeamWorkflowStatusDto replaceWithTeamWorkflowStatusDto = teamWorkflowStatusRetrieveService.retrieve(replaceWithTeamWorkflowStatusId);
        if (!teamWorkflowStatusDto.getTeamId().equalsIgnoreCase(replaceWithTeamWorkflowStatusDto.getTeamId())) {
            log.info("Workflow statuses are not in same team!");
            throw new NoAccessException();
        }
        if (!teamWorkflowStatusDto.getTeamId().equalsIgnoreCase(teamId)) {
            log.info("Workflow statuses are not in given team!");
            throw new NoAccessException();
        }
    }

    private InitializeTeamWorkflowStatusVo mapRequestToInitializeVo(InitializeTeamWorkflowStatusRequest initializeTeamWorkflowStatusRequest, TeamDto teamDto) {
        InitializeTeamWorkflowStatusVo initializeTeamWorkflowStatusVo = new InitializeTeamWorkflowStatusVo();
        initializeTeamWorkflowStatusVo.setTeamId(teamDto.getTeamId());
        initializeTeamWorkflowStatusVo.setWorkspaceId(teamDto.getWorkspaceId());
        initializeTeamWorkflowStatusVo.setWorkflowStateGroup(initializeTeamWorkflowStatusRequest.getWorkflowStateGroup());
        initializeTeamWorkflowStatusVo.setName(initializeTeamWorkflowStatusRequest.getName());
        return initializeTeamWorkflowStatusVo;
    }

    private TeamWorkflowStatusListingResponse mapResponse(List<TeamWorkflowStatusDto> teamWorkflowStatusDtos) {
        TeamWorkflowStatusListingResponse teamWorkflowStatusListingResponse = new TeamWorkflowStatusListingResponse();
        teamWorkflowStatusListingResponse.setTeamWorkflowStatusDtoList(teamWorkflowStatusDtos);
        return teamWorkflowStatusListingResponse;
    }
}
