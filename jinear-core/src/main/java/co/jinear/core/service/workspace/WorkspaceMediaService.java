package co.jinear.core.service.workspace;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.service.media.MediaOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static co.jinear.core.model.enumtype.media.MediaOwnerType.WORKSPACE;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMediaService {

    private final MediaOperationService mediaOperationService;

    public AccessibleMediaDto changeProfilePicture(MultipartFile logo, String workspaceId) {
        log.info("Change workspace profile picture has started. workspaceId: {}", workspaceId);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(logo, workspaceId);
        AccessibleMediaDto mediaDto = mediaOperationService.changeProfilePicture(initializeMediaVo);
        log.info("Change workspace profile picture has completed.");
        return mediaDto;
    }

    private InitializeMediaVo mapInitializeVo(MultipartFile file, String workspaceId) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(workspaceId);
        initializeMediaVo.setRelatedObjectId(workspaceId);
        initializeMediaVo.setFile(file);
        initializeMediaVo.setFileType(FileType.PROFILE_PIC);
        initializeMediaVo.setMediaOwnerType(WORKSPACE);
        initializeMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
        return initializeMediaVo;
    }
}
