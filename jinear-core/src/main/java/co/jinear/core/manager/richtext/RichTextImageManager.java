package co.jinear.core.manager.richtext;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileOwnershipStatusType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.response.richtext.RichTextTempImageResponse;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextImageManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceTierValidator workspaceTierValidator;
    private final MediaOperationService mediaOperationService;

    public RichTextTempImageResponse upload(String workspaceId, MultipartFile file) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccountAndWorkspaceTier(workspaceId, currentAccountId);
        log.info("Upload temp rich text image has started. currentAccountId: {}", currentAccountId);
        AccessibleMediaDto accessibleMediaDto = initializeMedia(file, currentAccountId, workspaceId);
        return mapResponse(accessibleMediaDto);
    }

    private void validateAccountAndWorkspaceTier(String workspaceId, String currentAccountId) {
        workspaceMemberService.isAccountWorkspaceMember(currentAccountId, workspaceId);
        workspaceTierValidator.validateWorkspaceHasFileUploadAccess(workspaceId);
    }

    private RichTextTempImageResponse mapResponse(AccessibleMediaDto accessibleMediaDto) {
        RichTextTempImageResponse richTextTempImageResponse = new RichTextTempImageResponse();
        richTextTempImageResponse.setAccessibleMediaDto(accessibleMediaDto);
        return richTextTempImageResponse;
    }

    private AccessibleMediaDto initializeMedia(MultipartFile file, String currentAccountId, String workspaceId) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(workspaceId);
        initializeMediaVo.setRelatedObjectId(currentAccountId);
        initializeMediaVo.setFile(file);
        initializeMediaVo.setFileType(FileType.RICH_TEXT_IMAGE);
        initializeMediaVo.setMediaOwnerType(MediaOwnerType.RICH_TEXT);
        initializeMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
        initializeMediaVo.setOwnershipStatus(MediaFileOwnershipStatusType.WAITING);
        return mediaOperationService.initializeMedia(initializeMediaVo);
    }
}
