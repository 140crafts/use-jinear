package co.jinear.core.manager.workspace;

import co.jinear.core.converter.workspace.WorkspaceInitializeVoConverter;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import co.jinear.core.model.request.workspace.WorkspaceTitleUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import co.jinear.core.model.response.workspace.WorkspaceDisplayPreferenceResponse;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.media.MediaValidator;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.workspace.*;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceManager {

    private final WorkspaceInitializeService workspaceInitializeService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final MediaRetrieveService mediaRetrieveService;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceInitializeVoConverter workspaceInitializeVoConverter;
    private final MediaValidator mediaValidator;
    private final WorkspaceMediaService workspaceMediaService;
    private final EntityManager entityManager;
    private final WorkspaceUpdateService workspaceUpdateService;

    public WorkspaceBaseResponse initializeWorkspace(MultipartFile logo, WorkspaceInitializeRequest workspaceInitializeRequest) {
        log.info("Initialize workspace has started with request: {}", workspaceInitializeRequest);
        String accountId = sessionInfoService.currentAccountId();
        Optional.ofNullable(logo).ifPresent(mediaValidator::validateForSafeImage);
        WorkspaceDto workspaceDto = initializeWorkspace(workspaceInitializeRequest, accountId);
        setLogoIfPresents(logo, workspaceDto);
        return mapValues(workspaceDto);
    }

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

    public WorkspaceDisplayPreferenceResponse updatePreferredWorkspace(String workspaceId) {
        String accountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        log.info("Update preferred workspace has started. workspaceId: {}, accountId: {}", workspaceId, accountId);
        workspaceDisplayPreferenceService.setAccountPreferredWorkspace(accountId, workspaceId);
        entityManager.clear();
        WorkspaceDisplayPreferenceDto workspaceDisplayPreferenceDto = workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspace(accountId);
        return mapResponse(workspaceDisplayPreferenceDto);
    }

    public WorkspaceDisplayPreferenceResponse updatePreferredTeam(String teamId) {
        String accountId = sessionInfoService.currentAccountId();
        teamAccessValidator.validateTeamAccess(accountId, teamId);
        log.info("Update preferred team has started. teamId: {}, accountId: {}", teamId, accountId);
        WorkspaceDisplayPreferenceDto workspaceDisplayPreferenceDto = workspaceDisplayPreferenceService.setAccountPreferredTeamId(accountId, teamId);
        return mapResponse(workspaceDisplayPreferenceDto);
    }

    public WorkspaceDisplayPreferenceResponse updatePreferredWorkspaceWithUsername(String workspaceUsername) {
        log.info("Update preferred workspace with username has started. workspaceUsername: {}", workspaceUsername);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        return updatePreferredWorkspace(workspaceDto.getWorkspaceId());
    }

    public WorkspaceDisplayPreferenceResponse updatePreferredTeamWithUsername(String workspaceUsername, String teamUsername) {
        log.info("Update preferred team with username has started. workspaceUsername: {}, teamUsername: {}", workspaceUsername, teamUsername);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceUsername);
        TeamDto teamDto = teamRetrieveService.retrieveActiveTeamByUsername(teamUsername, workspaceDto.getWorkspaceId());
        return updatePreferredTeam(teamDto.getTeamId());
    }

    public BaseResponse updateTitle(String workspaceId, WorkspaceTitleUpdateRequest workspaceTitleUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.isWorkspaceAdminOrOwner(currentAccountId, workspaceId);
        log.info("Update workspace title has started. currentAccountId: {}", currentAccountId);
        workspaceUpdateService.updateWorkspaceTitle(workspaceId, workspaceTitleUpdateRequest.getTitle());
        return new BaseResponse();
    }

    private WorkspaceDto initializeWorkspace(WorkspaceInitializeRequest workspaceInitializeRequest, String accountId) {
        WorkspaceInitializeVo workspaceInitializeVo = workspaceInitializeVoConverter.map(workspaceInitializeRequest, accountId);
        workspaceInitializeVo.setAppendRandomStrOnCollision(Boolean.FALSE);
        setHandleIfNotProvided(workspaceInitializeVo);
        return workspaceInitializeService.initializeWorkspace(workspaceInitializeVo);
    }

    private void setHandleIfNotProvided(WorkspaceInitializeVo workspaceInitializeVo) {
        String handle = Objects.isNull(workspaceInitializeVo.getHandle()) ?
                workspaceInitializeVo.getTitle().substring(0, Math.min(3, workspaceInitializeVo.getTitle().length())) :
                workspaceInitializeVo.getHandle();
        workspaceInitializeVo.setHandle(handle);
    }

    private WorkspaceBaseResponse mapValues(WorkspaceDto workspaceDto) {
        WorkspaceBaseResponse workspaceResponse = new WorkspaceBaseResponse();
        workspaceResponse.setWorkspace(workspaceDto);
        log.info("Initialize workspace has ended. workspaceResponse: {}", workspaceResponse);
        return workspaceResponse;
    }

    private void setLogoIfPresents(MultipartFile logo, WorkspaceDto workspaceDto) {
        Optional.ofNullable(logo)
                .map(file -> workspaceMediaService.changeProfilePicture(file, workspaceDto.getWorkspaceId()))
                .ifPresent(workspaceDto::setProfilePicture);
    }

    private WorkspaceDisplayPreferenceResponse mapResponse(WorkspaceDisplayPreferenceDto workspaceDisplayPreferenceDto) {
        WorkspaceDisplayPreferenceResponse workspaceDisplayPreferenceResponse = new WorkspaceDisplayPreferenceResponse();
        workspaceDisplayPreferenceResponse.setWorkspaceDisplayPreferenceDto(workspaceDisplayPreferenceDto);
        return workspaceDisplayPreferenceResponse;
    }
}
