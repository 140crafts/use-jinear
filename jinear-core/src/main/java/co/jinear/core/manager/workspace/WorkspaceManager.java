package co.jinear.core.manager.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import co.jinear.core.model.response.workspace.WorkspaceBaseResponse;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.model.vo.workspace.WorkspaceInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.workspace.WorkspaceActivityService;
import co.jinear.core.service.workspace.WorkspaceInitializeService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
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
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final MediaRetrieveService mediaRetrieveService;
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

    private WorkspaceDto initializeWorkspace(WorkspaceInitializeRequest workspaceInitializeRequest, String accountId) {
        WorkspaceInitializeVo workspaceInitializeVo = modelMapper.map(workspaceInitializeRequest, WorkspaceInitializeVo.class);
        workspaceInitializeVo.setOwnerId(accountId);
        workspaceInitializeVo.setAppendRandomStrOnCollision(Boolean.TRUE);
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
