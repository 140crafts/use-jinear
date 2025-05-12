package co.jinear.core.manager.workspace;

import co.jinear.core.config.properties.SseProperties;
import co.jinear.core.converter.team.TeamMembershipTeamVisibilityTypeMapConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.response.workspace.WorkspaceActivityListResponse;
import co.jinear.core.model.vo.workspace.WorkspaceActivityFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.activity.WorkspaceActivityFilterService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivitySseManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final SseProperties sseProperties;
    private final TeamMembershipTeamVisibilityTypeMapConverter teamMembershipTeamVisibilityTypeMapConverter;
    private final WorkspaceActivityFilterService workspaceActivityFilterService;

    public Flux<ServerSentEvent<WorkspaceActivityListResponse>> retrieveWorkspaceLatestActivity(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccountId, workspaceId);
        log.info("Retrieve workspace latest activity flux has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        return Flux.interval(Duration.ofSeconds(sseProperties.getLatestActivityInterval()))
                .map(sequence -> {
                    WorkspaceActivityListResponse workspaceActivityListResponse = retrieveWorkspaceLatestActivity(workspaceId, currentAccountId);
                    return ServerSentEvent.<WorkspaceActivityListResponse>builder()
                            .id(String.valueOf(sequence))
                            .event("workspace-activity")
                            .data(workspaceActivityListResponse)
                            .build();
                });
    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private WorkspaceActivityListResponse retrieveWorkspaceLatestActivity(String workspaceId, String accountId) {
        log.info("Retrieve workspace latest activity has started. accountId: {}", accountId);
        List<TeamMemberDto> memberships = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(accountId, workspaceId);
        Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap = teamMembershipTeamVisibilityTypeMapConverter.convert(memberships);
        WorkspaceActivityFilterVo workspaceActivityFilterVo = mapVo(workspaceId, teamMemberMap);
        Page<WorkspaceActivityDto> workspaceActivityDtoPage = workspaceActivityFilterService.filterActivities(workspaceActivityFilterVo);
        return mapResponse(workspaceActivityDtoPage);
    }

    private WorkspaceActivityFilterVo mapVo(String workspaceId, Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap) {
        WorkspaceActivityFilterVo workspaceActivityFilterVo = new WorkspaceActivityFilterVo();
        workspaceActivityFilterVo.setWorkspaceId(workspaceId);
        workspaceActivityFilterVo.setTeamMemberMap(teamMemberMap);
        workspaceActivityFilterVo.setSize(1);
        return workspaceActivityFilterVo;
    }

    private WorkspaceActivityListResponse mapResponse(Page<WorkspaceActivityDto> workspaceActivityDtoPage) {
        WorkspaceActivityListResponse workspaceActivityListResponse = new WorkspaceActivityListResponse();
        workspaceActivityListResponse.setWorkspaceActivityDtoPageDto(new PageDto<>(workspaceActivityDtoPage));
        return workspaceActivityListResponse;
    }
}
