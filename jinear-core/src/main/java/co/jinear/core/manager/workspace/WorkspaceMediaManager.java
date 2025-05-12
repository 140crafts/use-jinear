package co.jinear.core.manager.workspace;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaValidator;
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
public class WorkspaceMediaManager {

    private final MediaOperationService mediaOperationService;
    private final WorkspaceMemberService workspaceMemberService;
    private final SessionInfoService sessionInfoService;
    private final MediaValidator mediaValidator;

    public BaseResponse changeWorkspaceProfilePicture(MultipartFile file, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Change workspace picture has started. accountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        mediaValidator.validateForSafeImage(file);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(file, currentAccountId, workspaceId, MediaOwnerType.WORKSPACE);
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
        initializeMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
        return initializeMediaVo;
    }
}
