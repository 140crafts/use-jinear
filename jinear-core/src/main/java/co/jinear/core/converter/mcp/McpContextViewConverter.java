package co.jinear.core.converter.mcp;

import co.jinear.core.model.dto.account.AccountContextDto;
import co.jinear.core.model.dto.account.AccountContextTeamDto;
import co.jinear.core.model.dto.account.AccountContextWorkspaceDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.mcp.view.McpContextAccountView;
import co.jinear.core.model.mcp.view.McpContextDefaultsView;
import co.jinear.core.model.mcp.view.McpContextMemberView;
import co.jinear.core.model.mcp.view.McpContextTeamView;
import co.jinear.core.model.mcp.view.McpContextTopicView;
import co.jinear.core.model.mcp.view.McpContextView;
import co.jinear.core.model.mcp.view.McpContextWorkflowStatusView;
import co.jinear.core.model.mcp.view.McpContextWorkspaceView;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class McpContextViewConverter {

    public McpContextView context(AccountContextDto dto, Collection<String> grantedScopes) {
        McpContextAccountView account = account(dto);
        McpContextDefaultsView defaults = defaults(dto);
        List<String> sortedScopes = sorted(grantedScopes);
        List<McpContextWorkspaceView> workspaces = new ArrayList<>();
        for (AccountContextWorkspaceDto workspaceDto : dto.getWorkspaces()) {
            McpContextWorkspaceView workspace = workspace(workspaceDto);
            workspaces.add(workspace);
        }
        McpContextView view = new McpContextView();
        view.setAccount(account);
        view.setDefaults(defaults);
        view.setGrantedScopes(sortedScopes);
        view.setWorkspaces(workspaces);
        return view;
    }

    private McpContextAccountView account(AccountContextDto dto) {
        McpContextAccountView view = new McpContextAccountView();
        view.setAccountId(dto.getAccountId());
        view.setUsername(dto.getUsername());
        view.setLocale(name(dto.getLocaleType()));
        view.setTimeZone(dto.getTimeZone());
        view.setNow(offsetDateTime(dto.getNow()));
        return view;
    }

    private McpContextDefaultsView defaults(AccountContextDto dto) {
        McpContextDefaultsView view = new McpContextDefaultsView();
        view.setWorkspaceId(dto.getPreferredWorkspaceId());
        view.setTeamId(dto.getPreferredTeamId());
        return view;
    }

    private List<String> sorted(Collection<String> values) {
        List<String> sortedValues = new ArrayList<>();
        if (Objects.nonNull(values)) {
            sortedValues.addAll(values);
        }
        Collections.sort(sortedValues);
        return sortedValues;
    }

    private McpContextWorkspaceView workspace(AccountContextWorkspaceDto dto) {
        DetailedWorkspaceMemberDto membership = dto.getMembership();
        WorkspaceDto workspace = membership.getWorkspace();
        McpContextWorkspaceView view = new McpContextWorkspaceView();
        view.setWorkspaceId(membership.getWorkspaceId());
        view.setUsername(workspace.getUsername());
        view.setTitle(workspace.getTitle());
        view.setRole(name(membership.getRole()));
        view.setTier(name(workspace.getTier()));
        view.setMemberCount(dto.getMemberCount());
        if (Objects.nonNull(dto.getMembers())) {
            List<McpContextMemberView> members = members(dto.getMembers());
            view.setMembers(members);
        }
        List<McpContextTeamView> teams = new ArrayList<>();
        for (AccountContextTeamDto teamDto : dto.getTeams()) {
            McpContextTeamView team = team(teamDto);
            teams.add(team);
        }
        view.setTeams(teams);
        return view;
    }

    private List<McpContextMemberView> members(List<WorkspaceMemberDto> dtos) {
        List<McpContextMemberView> members = new ArrayList<>();
        for (WorkspaceMemberDto dto : dtos) {
            McpContextMemberView view = new McpContextMemberView();
            view.setAccountId(dto.getAccountId());
            if (Objects.nonNull(dto.getAccount())) {
                view.setUsername(dto.getAccount().getUsername());
            }
            members.add(view);
        }
        return members;
    }

    private McpContextTeamView team(AccountContextTeamDto dto) {
        TeamMemberDto membership = dto.getMembership();
        TeamDto team = membership.getTeam();
        List<McpContextWorkflowStatusView> statuses = workflowStatuses(dto.getWorkflowStatuses());
        List<McpContextTopicView> topics = topics(dto.getTopics());
        McpContextTeamView view = new McpContextTeamView();
        view.setTeamId(team.getTeamId());
        view.setName(team.getName());
        view.setTag(team.getTag());
        view.setRole(name(membership.getRole()));
        view.setStatuses(statuses);
        view.setTopics(topics);
        return view;
    }

    private List<McpContextWorkflowStatusView> workflowStatuses(List<TeamWorkflowStatusDto> dtos) {
        List<McpContextWorkflowStatusView> statuses = new ArrayList<>();
        for (TeamWorkflowStatusDto dto : dtos) {
            McpContextWorkflowStatusView view = new McpContextWorkflowStatusView();
            view.setWorkflowStatusId(dto.getTeamWorkflowStatusId());
            view.setName(dto.getName());
            view.setStateGroup(name(dto.getWorkflowStateGroup()));
            statuses.add(view);
        }
        return statuses;
    }

    private List<McpContextTopicView> topics(List<TopicDto> dtos) {
        List<McpContextTopicView> topics = new ArrayList<>();
        for (TopicDto dto : dtos) {
            McpContextTopicView view = new McpContextTopicView();
            view.setTopicId(dto.getTopicId());
            view.setName(dto.getName());
            topics.add(view);
        }
        return topics;
    }

    private String offsetDateTime(ZonedDateTime value) {
        if (Objects.isNull(value)) {
            return null;
        }
        ZonedDateTime truncated = value.truncatedTo(ChronoUnit.SECONDS);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(truncated);
    }

    private String name(Enum<?> value) {
        return Objects.isNull(value) ? null : value.name();
    }
}
