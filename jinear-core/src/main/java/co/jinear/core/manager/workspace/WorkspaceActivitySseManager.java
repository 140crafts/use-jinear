package co.jinear.core.manager.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.response.workspace.WorkspaceActivityResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.activity.WorkspaceActivityRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivitySseManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceActivityRetrieveService workspaceActivityRetrieveService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;

    public Flux<ServerSentEvent<WorkspaceActivityResponse>> retrieveWorkspaceLatestActivity(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve workspace latest activity flux has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        return Flux.interval(Duration.ofSeconds(60))
                .map(sequence -> {
                    WorkspaceActivityResponse workspaceActivityResponse = retrieveWorkspaceLatestActivity(workspaceId, currentAccountId);
                    return ServerSentEvent.<WorkspaceActivityResponse>builder()
                            .id(String.valueOf(sequence))
                            .event("workspace-activity")
                            .data(workspaceActivityResponse)
                            .build();
                });
    }

    private WorkspaceActivityResponse retrieveWorkspaceLatestActivity(String workspaceId, String accountId) {
        log.info("Retrieve workspace latest activity has started. accountId: {}", accountId);
        List<String> accountTeamIds = teamMemberRetrieveService.retrieveAllTeamIdsOfAnAccount(accountId, workspaceId);
        WorkspaceActivityDto latestActivity = workspaceActivityRetrieveService.retrieveWorkspaceLatestActivity(workspaceId, accountTeamIds);
        return mapWorkspaceActivityResponse(latestActivity);
    }

    private WorkspaceActivityResponse mapWorkspaceActivityResponse(WorkspaceActivityDto workspaceActivityDto) {
        WorkspaceActivityResponse workspaceActivityResponse = new WorkspaceActivityResponse();
        workspaceActivityResponse.setWorkspaceActivityDto(workspaceActivityDto);
        return workspaceActivityResponse;
    }
}
