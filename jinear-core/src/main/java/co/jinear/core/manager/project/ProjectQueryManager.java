package co.jinear.core.manager.project;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.project.ProjectListingPaginatedResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.ProjectListingService;
import co.jinear.core.service.project.ProjectTeamListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectQueryManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final ProjectTeamListingService projectTeamListingService;
    private final ProjectListingService projectListingService;

    public ProjectListingPaginatedResponse retrieveAssigned(String workspaceId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve assigned projects has started. currentAccountId: {}, workspaceId: {}, page: {}", currentAccountId, workspaceId, page);
        List<TeamMemberDto> teamMemberDtos = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccountId, workspaceId);
        List<String> teamMemberships = teamMemberDtos.stream().map(TeamMemberDto::getTeamId).toList();
        Page<ProjectDto> projectsPage = projectTeamListingService.retrieveAllByTeamIdOrTeamIdEmpty(teamMemberships, page);
        return mapResponse(projectsPage);
    }

    public ProjectListingPaginatedResponse retrieveAll(String workspaceId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve all projects has started. currentAccountId: {}, workspaceId: {}, page: {}", currentAccountId, workspaceId, page);
        Page<ProjectDto> projectsPage = projectListingService.retrieveWorkspaceProjects(workspaceId, page);
        return mapResponse(projectsPage);
    }

    private ProjectListingPaginatedResponse mapResponse(Page<ProjectDto> projectsPage) {
        ProjectListingPaginatedResponse projectListingPaginatedResponse = new ProjectListingPaginatedResponse();
        projectListingPaginatedResponse.setProjects(new PageDto<>(projectsPage));
        return projectListingPaginatedResponse;
    }
}
