package co.jinear.core.manager.account;

import co.jinear.core.model.dto.account.AccountContextDto;
import co.jinear.core.model.dto.account.AccountContextTeamDto;
import co.jinear.core.model.dto.account.AccountContextWorkspaceDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.enumtype.team.TeamStateType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.workspace.WorkspaceDisplayPreferenceService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import co.jinear.core.system.util.ZonedDateHelper;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountContextManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TopicRetrieveService topicRetrieveService;

    public AccountContextDto retrieveAccountContext(String workspaceId, boolean includeMembers) {
        String currentAccountId = sessionInfoService.currentAccountId();
        if (Objects.nonNull(workspaceId)) {
            workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        }
        log.info("Retrieve account context has started. accountId: {}, workspaceId: {}, includeMembers: {}", currentAccountId, workspaceId, includeMembers);
        List<DetailedWorkspaceMemberDto> workspaceMemberships = workspaceMemberListingService.retrieveAccountsWorkspaceMemberships(currentAccountId);
        List<TeamMemberDto> allTeamMemberships = teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccountId);
        List<TeamMemberDto> teamMemberships = activeTeamMemberships(allTeamMemberships);

        AccountContextDto accountContextDto = new AccountContextDto();
        populateAccount(accountContextDto, currentAccountId);
        populateDefaults(accountContextDto, currentAccountId, workspaceMemberships, teamMemberships);
        populateWorkspaces(accountContextDto, workspaceMemberships, teamMemberships, workspaceId, includeMembers);
        log.info("Retrieve account context has finished. workspaceCount: {}", accountContextDto.getWorkspaces().size());
        return accountContextDto;
    }

    private List<TeamMemberDto> activeTeamMemberships(List<TeamMemberDto> teamMemberships) {
        List<TeamMemberDto> active = new ArrayList<>();
        for (TeamMemberDto teamMembership : teamMemberships) {
            if (TeamStateType.ACTIVE.equals(teamMembership.getTeam().getTeamState())) {
                active.add(teamMembership);
            }
        }
        return active;
    }

    private void populateAccount(AccountContextDto accountContextDto, String accountId) {
        AccountDto accountDto = accountRetrieveService.retrieve(accountId);
        ZoneId accountZoneId = ZonedDateHelper.zoneIdOrUtc(accountDto.getTimeZone());
        String ianaTimeZone = ZonedDateHelper.ianaZoneIdOf(accountZoneId);
        ZonedDateTime now = ZonedDateTime.now(accountZoneId);
        accountContextDto.setAccountId(accountDto.getAccountId());
        accountContextDto.setUsername(accountDto.getUsername());
        accountContextDto.setLocaleType(accountDto.getLocaleType());
        accountContextDto.setTimeZone(ianaTimeZone);
        accountContextDto.setNow(now);
    }

    private void populateDefaults(AccountContextDto accountContextDto, String accountId,
                                  List<DetailedWorkspaceMemberDto> workspaceMemberships,
                                  List<TeamMemberDto> teamMemberships) {
        Optional<WorkspaceDisplayPreferenceDto> preference = workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspaceOptional(accountId);
        if (preference.isEmpty()) {
            return;
        }
        String preferredWorkspaceId = preference.get().getPreferredWorkspaceId();
        String preferredTeamId = preference.get().getPreferredTeamId();
        if (isWorkspaceMember(workspaceMemberships, preferredWorkspaceId)) {
            accountContextDto.setPreferredWorkspaceId(preferredWorkspaceId);
        }
        if (isTeamMemberWithinWorkspace(teamMemberships, preferredTeamId, accountContextDto.getPreferredWorkspaceId())) {
            accountContextDto.setPreferredTeamId(preferredTeamId);
        }
    }

    private boolean isWorkspaceMember(List<DetailedWorkspaceMemberDto> workspaceMemberships, String workspaceId) {
        for (DetailedWorkspaceMemberDto workspaceMembership : workspaceMemberships) {
            if (workspaceMembership.getWorkspaceId().equals(workspaceId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTeamMemberWithinWorkspace(List<TeamMemberDto> teamMemberships, String teamId, String workspaceId) {
        for (TeamMemberDto teamMembership : teamMemberships) {
            if (teamMembership.getTeamId().equals(teamId) && teamMembership.getWorkspaceId().equals(workspaceId)) {
                return true;
            }
        }
        return false;
    }

    private void populateWorkspaces(AccountContextDto accountContextDto,
                                    List<DetailedWorkspaceMemberDto> workspaceMemberships,
                                    List<TeamMemberDto> teamMemberships,
                                    String workspaceId, boolean includeMembers) {
        List<AccountContextWorkspaceDto> workspaces = new ArrayList<>();
        for (DetailedWorkspaceMemberDto membership : workspaceMemberships) {
            if (Objects.nonNull(workspaceId) && !membership.getWorkspaceId().equals(workspaceId)) {
                continue;
            }
            AccountContextWorkspaceDto accountContextWorkspaceDto = new AccountContextWorkspaceDto();
            populateWorkspace(accountContextWorkspaceDto, membership, includeMembers);
            populateTeams(accountContextWorkspaceDto, teamMemberships);
            workspaces.add(accountContextWorkspaceDto);
        }
        accountContextDto.setWorkspaces(workspaces);
    }

    private void populateWorkspace(AccountContextWorkspaceDto accountContextWorkspaceDto, DetailedWorkspaceMemberDto membership, boolean includeMembers) {
        Long memberCount = workspaceMemberListingService.workspaceActiveMemberCount(membership.getWorkspaceId());
        accountContextWorkspaceDto.setMembership(membership);
        accountContextWorkspaceDto.setMemberCount(memberCount);
        if (includeMembers) {
            List<WorkspaceMemberDto> members = workspaceMemberListingService.listAllWorkspaceMembers(membership.getWorkspaceId());
            accountContextWorkspaceDto.setMembers(members);
        }
    }

    private void populateTeams(AccountContextWorkspaceDto accountContextWorkspaceDto, List<TeamMemberDto> teamMemberships) {
        String workspaceId = accountContextWorkspaceDto.getMembership().getWorkspaceId();
        List<AccountContextTeamDto> teams = new ArrayList<>();
        for (TeamMemberDto teamMembership : teamMemberships) {
            if (!teamMembership.getWorkspaceId().equals(workspaceId)) {
                continue;
            }
            AccountContextTeamDto accountContextTeamDto = new AccountContextTeamDto();
            populateTeam(accountContextTeamDto, teamMembership);
            teams.add(accountContextTeamDto);
        }
        accountContextWorkspaceDto.setTeams(teams);
    }

    private void populateTeam(AccountContextTeamDto accountContextTeamDto, TeamMemberDto teamMembership) {
        TeamDto team = teamMembership.getTeam();
        List<TeamWorkflowStatusDto> workflowStatuses = workflowStatusesInBoardOrder(team.getWorkflowStatuses());
        List<TopicDto> topics = topicRetrieveService.retrieveAllTeamTopics(team.getTeamId());
        accountContextTeamDto.setMembership(teamMembership);
        accountContextTeamDto.setWorkflowStatuses(workflowStatuses);
        accountContextTeamDto.setTopics(topics);
    }

    private List<TeamWorkflowStatusDto> workflowStatusesInBoardOrder(Set<TeamWorkflowStatusDto> workflowStatuses) {
        List<TeamWorkflowStatusDto> ordered = new ArrayList<>();
        if (Objects.isNull(workflowStatuses)) {
            return ordered;
        }
        for (TeamWorkflowStateGroup workflowStateGroup : TeamWorkflowStateGroup.values()) {
            List<TeamWorkflowStatusDto> groupStatuses = statusesOfGroupByOrder(workflowStatuses, workflowStateGroup);
            ordered.addAll(groupStatuses);
        }
        return ordered;
    }

    private List<TeamWorkflowStatusDto> statusesOfGroupByOrder(Set<TeamWorkflowStatusDto> workflowStatuses, TeamWorkflowStateGroup workflowStateGroup) {
        List<TeamWorkflowStatusDto> groupStatuses = new ArrayList<>();
        for (TeamWorkflowStatusDto workflowStatus : workflowStatuses) {
            if (workflowStateGroup.equals(workflowStatus.getWorkflowStateGroup())) {
                insertByOrder(groupStatuses, workflowStatus);
            }
        }
        return groupStatuses;
    }

    private void insertByOrder(List<TeamWorkflowStatusDto> groupStatuses, TeamWorkflowStatusDto workflowStatus) {
        int index = 0;
        while (index < groupStatuses.size() && orderOf(groupStatuses.get(index)) <= orderOf(workflowStatus)) {
            index++;
        }
        groupStatuses.add(index, workflowStatus);
    }

    private int orderOf(TeamWorkflowStatusDto workflowStatus) {
        return Objects.isNull(workflowStatus.getOrder()) ? Integer.MAX_VALUE : workflowStatus.getOrder();
    }
}
