package co.jinear.core.service.workspace;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.username.UsernameDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import co.jinear.core.model.enumtype.username.UsernameRelatedObjectType;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.model.vo.username.InitializeUsernameVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import co.jinear.core.model.vo.workspace.WorkspaceSettingsInitializeVo;
import co.jinear.core.repository.WorkspaceRepository;
import co.jinear.core.service.slack.SlackService;
import co.jinear.core.service.team.TeamInitializeService;
import co.jinear.core.service.username.UsernameService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.system.NormalizeHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceInitializeService {

    private final WorkspaceRepository workspaceRepository;
    private final UsernameService usernameService;
    private final WorkspaceSettingService workspaceSettingService;
    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final TeamInitializeService teamInitializeService;
    private final SlackService slackService;

    @Transactional
    public WorkspaceDto initializeWorkspace(WorkspaceInitializeVo workspaceInitializeVo) {
        log.info("Initialize workspace has started. workspaceInitializeVo: {}", workspaceInitializeVo);
        Workspace workspace = saveWorkspace(workspaceInitializeVo);
        UsernameDto usernameDto = assignUsername(workspaceInitializeVo, workspace);
        WorkspaceSettingDto workspaceSettingDto = initializeSettings(workspaceInitializeVo, workspace);
        assignOwner(workspaceInitializeVo, workspace);
        TeamDto initialTeamDto = createInitialTeam(workspace, workspaceInitializeVo);
        setAsDefaultWorkspace(workspaceInitializeVo.getOwnerId(), initialTeamDto);
        slackService.sendEventMessage(String.format("New Workspace with username: %s", usernameDto.getUsername()));
        return mapValues(workspace, usernameDto, workspaceSettingDto);
    }

    private void setAsDefaultWorkspace(String ownerId, TeamDto initialTeamDto) {
        workspaceDisplayPreferenceService.setAccountPreferredWorkspace(ownerId, initialTeamDto.getWorkspaceId());
        workspaceDisplayPreferenceService.setAccountPreferredTeamId(ownerId, initialTeamDto.getTeamId());
    }

    private TeamDto createInitialTeam(Workspace workspace, WorkspaceInitializeVo workspaceInitializeVo) {
        log.info("Create initial team has started for workspaceId: {}", workspace.getWorkspaceId());

        String teamTag = NormalizeHelper.normalizeStrictly(workspace.getTitle());
        teamTag = teamTag.substring(0, Math.min(teamTag.length(), 3));

        String teamUsername = NormalizeHelper.normalizeStrictly(workspace.getTitle());
        teamUsername = teamUsername.substring(0, Math.min(teamTag.length(), 3));

        TeamInitializeVo teamInitializeVo = new TeamInitializeVo();
        teamInitializeVo.setWorkspaceId(workspace.getWorkspaceId());
        teamInitializeVo.setName(workspace.getTitle());
        teamInitializeVo.setTag(teamTag);
        teamInitializeVo.setUsername(teamUsername);
        teamInitializeVo.setVisibility(TeamVisibilityType.VISIBLE);
        teamInitializeVo.setJoinMethod(TeamJoinMethodType.SYNC_MEMBERS_WITH_WORKSPACE);
        teamInitializeVo.setTaskVisibility(TeamTaskVisibilityType.VISIBLE_TO_ALL_TEAM_MEMBERS);
        teamInitializeVo.setLocale(workspaceInitializeVo.getLocale());
        teamInitializeVo.setInitializedBy(workspaceInitializeVo.getOwnerId());
        return teamInitializeService.initializeTeam(teamInitializeVo);
    }

    private void assignOwner(WorkspaceInitializeVo workspaceInitializeVo, Workspace workspace) {
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = new InitializeWorkspaceMemberVo();
        initializeWorkspaceMemberVo.setAccountId(workspaceInitializeVo.getOwnerId());
        initializeWorkspaceMemberVo.setWorkspaceId(workspace.getWorkspaceId());
        initializeWorkspaceMemberVo.setRole(WorkspaceAccountRoleType.OWNER);
        workspaceMemberService.initializeWorkspaceMember(initializeWorkspaceMemberVo);
    }

    private WorkspaceSettingDto initializeSettings(WorkspaceInitializeVo workspaceInitializeVo, Workspace workspace) {
        WorkspaceSettingsInitializeVo workspaceSettingsInitializeVo = new WorkspaceSettingsInitializeVo();
        workspaceSettingsInitializeVo.setWorkspaceId(workspace.getWorkspaceId());
        workspaceSettingsInitializeVo.setVisibility(workspaceInitializeVo.getVisibility());
        workspaceSettingsInitializeVo.setJoinType(workspaceInitializeVo.getJoinType());
        return workspaceSettingService.initializeWorkspaceSettings(workspaceSettingsInitializeVo);
    }

    private UsernameDto assignUsername(WorkspaceInitializeVo workspaceInitializeVo, Workspace workspace) {
        Boolean appendRandomStrOnCollision = Optional.of(workspaceInitializeVo)
                .map(WorkspaceInitializeVo::getAppendRandomStrOnCollision)
                .orElse(Boolean.TRUE);
        InitializeUsernameVo initializeUsernameVo = new InitializeUsernameVo();
        initializeUsernameVo.setRelatedObjectId(workspace.getWorkspaceId());
        initializeUsernameVo.setRelatedObjectType(UsernameRelatedObjectType.WORKSPACE);
        initializeUsernameVo.setUsername(workspaceInitializeVo.getHandle());
        initializeUsernameVo.setAppendRandomStrOnCollision(appendRandomStrOnCollision);
        return usernameService.assignUsername(initializeUsernameVo);
    }

    private Workspace saveWorkspace(WorkspaceInitializeVo workspaceInitializeVo) {
        Workspace workspace = new Workspace();
        workspace.setTitle(workspaceInitializeVo.getTitle());
        workspace.setDescription(workspaceInitializeVo.getDescription());
//        workspace.setTier(WorkspaceTier.BASIC);
        workspace.setTier(WorkspaceTier.PRO);
        return workspaceRepository.saveAndFlush(workspace);
    }

    private WorkspaceDto mapValues(Workspace workspace, UsernameDto usernameDto, WorkspaceSettingDto workspaceSettingDto) {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setWorkspaceId(workspace.getWorkspaceId());
        workspaceDto.setTitle(workspace.getTitle());
        workspaceDto.setDescription(workspace.getDescription());
        workspaceDto.setTier(workspace.getTier());
        workspaceDto.setUsername(usernameDto.getUsername());
        workspaceDto.setSettings(workspaceSettingDto);
        return workspaceDto;
    }
}
