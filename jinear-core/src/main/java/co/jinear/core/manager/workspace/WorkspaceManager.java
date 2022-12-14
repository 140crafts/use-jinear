package co.jinear.core.manager.workspace;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.workspace.WorkspaceActivityService;
import co.jinear.core.service.workspace.WorkspaceDisplayPreferenceService;
import co.jinear.core.service.workspace.WorkspaceInitializeService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceManager {

    private final WorkspaceInitializeService workspaceInitializeService;
    private final WorkspaceActivityService workspaceActivityService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final MediaRetrieveService mediaRetrieveService;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final TeamRetrieveService teamRetrieveService;
    private final ModelMapper modelMapper;

    public WorkspaceBaseResponse retrieveWorkspaceWithUsername(String workspaceUsername) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve workspace by username has started. workspaceUsername: {}, currentAccountId: {}", workspaceUsername, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        workspaceValidator.validateHasAccess(currentAccountId, workspaceDto);
        mediaRetrieveService.retrieveProfilePictureOptional(workspaceDto.getWorkspaceId()).ifPresent(workspaceDto::setProfilePicture);
        return mapValues(workspaceDto);
    }

    public WorkspaceBaseResponse retrieveWorkspaceWithId(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve workspace by id has started. workspaceId: {}, currentAccountId: {}", workspaceId, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        workspaceValidator.validateHasAccess(currentAccountId, workspaceDto);
        mediaRetrieveService.retrieveProfilePictureOptional(workspaceDto.getWorkspaceId()).ifPresent(workspaceDto::setProfilePicture);
        return mapValues(workspaceDto);
    }

    public WorkspaceBaseResponse initializeWorkspace(WorkspaceInitializeRequest workspaceInitializeRequest) {
        log.info("Initialize workspace has started with request: {}", workspaceInitializeRequest);
        String accountId = sessionInfoService.currentAccountId();
        WorkspaceDto workspaceDto = initializeWorkspace(workspaceInitializeRequest, accountId);
        createWorkspaceActivity(accountId, workspaceDto);
        return mapValues(workspaceDto);
    }

    public BaseResponse updatePreferredWorkspace(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        log.info("Update preferred workspace has started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        workspaceDisplayPreferenceService.setAccountPreferredWorkspace(accountId, workspaceId);
        return new BaseResponse();
    }

    public BaseResponse updatePreferredTeam(String teamId) {
        String accountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(accountId, teamId);
        log.info("Update preferred team has started. teamId: {}, accountId: {}", teamId, accountId);
        workspaceDisplayPreferenceService.setAccountPreferredTeamId(accountId, teamId);
        return new BaseResponse();
    }

    public BaseResponse updatePreferredWorkspaceWithUsername(String workspaceUsername) {
        log.info("Update preferred workspace with username has started. workspaceUsername: {}", workspaceUsername);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        return updatePreferredWorkspace(workspaceDto.getWorkspaceId());
    }

    public BaseResponse updatePreferredTeamWithUsername(String workspaceUsername, String teamName) {
        log.info("Update preferred team with username has started. workspaceUsername: {}, teamName: {}", workspaceUsername, teamName);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        TeamDto teamDto = teamRetrieveService.retrieveActiveTeamByName(teamName, workspaceDto.getWorkspaceId());
        return updatePreferredTeam(teamDto.getTeamId());
    }

    private WorkspaceDto initializeWorkspace(WorkspaceInitializeRequest workspaceInitializeRequest, String accountId) {
        WorkspaceInitializeVo workspaceInitializeVo = modelMapper.map(workspaceInitializeRequest, WorkspaceInitializeVo.class);
        workspaceInitializeVo.setOwnerId(accountId);
        workspaceInitializeVo.setAppendRandomStrOnCollision(Boolean.TRUE);
        workspaceInitializeVo.setIsPersonal(Boolean.FALSE);
        setHandleIfNotProvided(workspaceInitializeVo);
        return workspaceInitializeService.initializeWorkspace(workspaceInitializeVo);
    }

    private void setHandleIfNotProvided(WorkspaceInitializeVo workspaceInitializeVo) {
        String handle = Objects.isNull(workspaceInitializeVo.getHandle()) ?
                workspaceInitializeVo.getTitle().substring(0, Math.min(3, workspaceInitializeVo.getTitle().length())) :
                workspaceInitializeVo.getHandle();
        workspaceInitializeVo.setHandle(handle);
    }

    private void createWorkspaceActivity(String accountId, WorkspaceDto workspaceDto) {
        WorkspaceActivityCreateVo workspaceActivityCreateVo = new WorkspaceActivityCreateVo();
        workspaceActivityCreateVo.setWorkspaceId(workspaceDto.getWorkspaceId());
        workspaceActivityCreateVo.setAccountId(accountId);
        workspaceActivityCreateVo.setRelatedObjectId(accountId);
        workspaceActivityCreateVo.setType(WorkspaceActivityType.JOIN);
        workspaceActivityService.createWorkspaceActivity(workspaceActivityCreateVo);
    }

    private WorkspaceBaseResponse mapValues(WorkspaceDto workspaceDto) {
        WorkspaceBaseResponse workspaceResponse = new WorkspaceBaseResponse();
        workspaceResponse.setWorkspace(workspaceDto);
        log.info("Initialize workspace has ended. workspaceResponse: {}", workspaceResponse);
        return workspaceResponse;
    }
}
