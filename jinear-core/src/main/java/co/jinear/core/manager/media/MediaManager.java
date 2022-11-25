package co.jinear.core.manager.media;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaManager {

    private final MediaOperationService mediaOperationService;
    private final WorkspaceMemberService workspaceMemberService;
    private final SessionInfoService sessionInfoService;

    public BaseResponse changeAccountProfilePicture(MultipartFile file) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Change profile picture has started. accountId: {}", currentAccountId);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(file, currentAccountId, currentAccountId, MediaOwnerType.USER);
        mediaOperationService.changeProfilePicture(initializeMediaVo);
        log.info("Change profile picture has finished. accountId: {}", currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse changeWorkspaceProfilePicture(MultipartFile file, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Change workspace picture has started. accountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(file, currentAccountId, workspaceId, MediaOwnerType.USER);
        workspaceMemberService.validateAccountHasRoleInWorkspace(currentAccountId, workspaceId, List.of(OWNER, ADMIN));
        mediaOperationService.changeProfilePicture(initializeMediaVo);
        log.info("Change workspace picture has finished. accountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        return new BaseResponse();
    }

    private InitializeMediaVo mapInitializeVo(MultipartFile file, String ownerId, String relatedObjectId, MediaOwnerType mediaOwnerType) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(ownerId);
        initializeMediaVo.setRelatedObjectId(relatedObjectId);
        initializeMediaVo.setFile(file);
        initializeMediaVo.setFileType(FileType.PROFILE_PIC);
        initializeMediaVo.setMediaOwnerType(mediaOwnerType);
        return initializeMediaVo;
    }
}
