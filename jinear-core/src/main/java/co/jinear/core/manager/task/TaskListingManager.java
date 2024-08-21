package co.jinear.core.manager.task;

import co.jinear.core.converter.task.TaskFilterRequestConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.dto.project.ProjectTeamDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.response.task.TaskListingListedResponse;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.MilestoneRetrieveService;
import co.jinear.core.service.project.ProjectTeamListingService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TaskListingService taskListingService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TaskFilterRequestConverter taskFilterRequestConverter;
    private final ProjectTeamListingService projectTeamListingService;
    private final MilestoneRetrieveService milestoneRetrieveService;

    public TaskListingPaginatedResponse filterTasks(TaskFilterRequest taskFilterRequest) {
        String currentAccount = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(currentAccount, taskFilterRequest.getWorkspaceId());

        validateTeamIdListHasNoBlankElement(taskFilterRequest);
        List<TeamMemberDto> memberships = retrieveMemberships(taskFilterRequest, currentAccount);
        validateAccountMembershipsInRequestedTeams(taskFilterRequest, memberships);
        validateTeamTaskVisibilityAndMemberRoleForAll(memberships);
        validateProjectAndMilestoneAccess(taskFilterRequest, memberships);

        TaskSearchFilterVo taskSearchFilterVo = taskFilterRequestConverter.convert(taskFilterRequest, memberships);
        Page<TaskDto> taskDtoPage = taskListingService.filterTasks(taskSearchFilterVo);
        return mapResponse(taskDtoPage);
    }

    private void validateProjectAndMilestoneAccess(TaskFilterRequest taskFilterRequest, List<TeamMemberDto> memberships) {
        List<String> requestedProjectIds = taskFilterRequest.getProjectIds();
        List<String> requestedMilestoneIds = taskFilterRequest.getMilestoneIds();

        if (Objects.nonNull(requestedProjectIds) || Objects.nonNull(requestedMilestoneIds)){
            List<String> membershipTeamIds = memberships.stream()
                    .map(TeamMemberDto::getTeamId)
                    .toList();
            List<ProjectTeamDto> teamMembershipsProjects = projectTeamListingService.retrieveAllByTeamIdOrTeamIdEmpty(membershipTeamIds);

            if (Objects.nonNull(requestedProjectIds)) {
                List<String> teamMembershipsProjectsTeamIds = teamMembershipsProjects.stream()
                        .map(ProjectTeamDto::getTeamId)
                        .toList();
                requestedProjectIds.forEach(requestedProjectId -> {
                    if (!teamMembershipsProjectsTeamIds.contains(requestedProjectId)) {
                        throw new NoAccessException();
                    }
                });
            }

            if (Objects.nonNull(requestedMilestoneIds)) {
                List<String> participatedProjectIds = teamMembershipsProjects.stream().map(ProjectTeamDto::getProjectId).toList();
                List<MilestoneDto> participatedMilestones = milestoneRetrieveService.retrieveAllByProjectIds(participatedProjectIds);
                List<String> participatedMilestoneIds = participatedMilestones.stream()
                        .map(MilestoneDto::getMilestoneId)
                        .toList();
                requestedMilestoneIds.forEach(requestedMilestoneId -> {
                    if (!participatedMilestoneIds.contains(requestedMilestoneId)) {
                        throw new NoAccessException();
                    }
                });
            }
        }
    }

    private List<TeamMemberDto> retrieveMemberships(TaskFilterRequest taskFilterRequest, String currentAccount) {
        String workspaceId = taskFilterRequest.getWorkspaceId();
        return Optional.of(taskFilterRequest)
                .map(TaskFilterRequest::getTeamIdList)
                .filter(teamIds -> !teamIds.isEmpty())
                .map(teamIds -> teamMemberRetrieveService.retrieveMemberships(workspaceId, currentAccount, teamIds))
                .orElseGet(() -> teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, workspaceId));

    }

    private void validateWorkspaceAccess(String accountId, String workspaceId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
    }

    private void validateTeamTaskVisibilityAndMemberRoleForAll(List<TeamMemberDto> memberships) {
        memberships.forEach(this::validateTeamTaskVisibilityAndMemberRole);
    }

    private void validateTeamTaskVisibilityAndMemberRole(TeamMemberDto teamMemberDto) {
        TeamDto teamDto = teamMemberDto.getTeam();
        TeamMemberRoleType role = teamMemberDto.getRole();

        if (OWNER_ASSIGNEE_AND_ADMINS.equals(teamDto.getTaskVisibility()) && !List.of(TeamMemberRoleType.ADMIN, TeamMemberRoleType.MEMBER).contains(role)) {
            throw new NoAccessException();
        }
    }

    private void validateAccountMembershipsInRequestedTeams(TaskFilterRequest taskFilterRequest, List<TeamMemberDto> memberships) {
        List<String> teamIdList = taskFilterRequest.getTeamIdList();
        if (Objects.nonNull(teamIdList)) {
            List<String> membershipTeamIds = memberships.stream().map(TeamMemberDto::getTeamId).toList();
            if (teamIdList.size() > membershipTeamIds.size()) {
                throw new NoAccessException();
            }
            teamIdList.forEach(teamId -> {
                if (!membershipTeamIds.contains(teamId)) {
                    throw new NoAccessException();
                }
            });
        }
    }

    private void validateTeamIdListHasNoBlankElement(TaskFilterRequest taskFilterRequest) {
        Optional.of(taskFilterRequest)
                .map(TaskFilterRequest::getTeamIdList)
                .map(Collection::stream)
                .filter(teamIdStream -> teamIdStream.anyMatch(Strings::isBlank))
                .ifPresent(teamIdStream -> {
                    throw new NotValidException();
                });
    }

    private TaskListingPaginatedResponse mapResponse(Page<TaskDto> taskDtoPage) {
        TaskListingPaginatedResponse taskListingPaginatedResponse = new TaskListingPaginatedResponse();
        taskListingPaginatedResponse.setTaskDtoPage(new PageDto<>(taskDtoPage));
        return taskListingPaginatedResponse;
    }

    private TaskListingListedResponse mapResponse(List<TaskDto> allTasks) {
        TaskListingListedResponse taskListingListedResponse = new TaskListingListedResponse();
        taskListingListedResponse.setTaskDtoList(allTasks);
        return taskListingListedResponse;
    }
}
