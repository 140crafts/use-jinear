package co.jinear.core.service.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.dto.username.UsernameDto;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.username.UsernameRelatedObjectType;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import co.jinear.core.model.vo.workspace.WorkspaceSettingsInitializeVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.model.vo.username.InitializeUsernameVo;
import co.jinear.core.repository.WorkspaceRepository;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.service.username.UsernameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceInitializeService {

    private final WorkspaceRepository workspaceRepository;
    private final UsernameService usernameService;
    private final WorkspaceSettingService workspaceSettingService;
    private final WorkspaceMemberService workspaceMemberService;

    @Transactional
    public WorkspaceDto initializeWorkspace(WorkspaceInitializeVo workspaceInitializeVo) {
        log.info("Initialize workspace has started. workspaceInitializeVo: {}", workspaceInitializeVo);
        Workspace workspace = saveWorkspace(workspaceInitializeVo);
        UsernameDto usernameDto = assignUsername(workspaceInitializeVo, workspace);
        WorkspaceSettingDto workspaceSettingDto = initializeSettings(workspaceInitializeVo, workspace);
        assignOwner(workspaceInitializeVo, workspace);
        return mapValues(workspace, usernameDto, workspaceSettingDto);
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
        Boolean appendRandomStrOnCollision = Objects.isNull(workspaceInitializeVo.getAppendRandomStrOnCollision()) ?
                Boolean.FALSE : workspaceInitializeVo.getAppendRandomStrOnCollision();
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
        workspace.setIsPersonal(workspaceInitializeVo.getIsPersonal());
        return workspaceRepository.save(workspace);
    }

    private WorkspaceDto mapValues(Workspace workspace, UsernameDto usernameDto, WorkspaceSettingDto workspaceSettingDto) {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setWorkspaceId(workspace.getWorkspaceId());
        workspaceDto.setTitle(workspace.getTitle());
        workspaceDto.setDescription(workspace.getDescription());
        workspaceDto.setUsername(usernameDto.getUsername());
        workspaceDto.setSettings(workspaceSettingDto);
        return workspaceDto;
    }
}
