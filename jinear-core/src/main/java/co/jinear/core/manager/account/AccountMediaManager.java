package co.jinear.core.manager.account;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountMediaManager {

    private final MediaOperationService mediaOperationService;
    private final SessionInfoService sessionInfoService;
    private final MediaValidator mediaValidator;

    public BaseResponse changeAccountProfilePicture(MultipartFile file) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Change profile picture has started. accountId: {}", currentAccountId);
        mediaValidator.validateForSafeImage(file);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(file, currentAccountId, currentAccountId);
        mediaOperationService.changeProfilePicture(initializeMediaVo);
        log.info("Change profile picture has finished. accountId: {}", currentAccountId);
        return new BaseResponse();
    }

    private InitializeMediaVo mapInitializeVo(MultipartFile file, String ownerId, String relatedObjectId) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(ownerId);
        initializeMediaVo.setRelatedObjectId(relatedObjectId);
        initializeMediaVo.setFile(file);
        initializeMediaVo.setFileType(FileType.PROFILE_PIC);
        initializeMediaVo.setMediaOwnerType(MediaOwnerType.USER);
        initializeMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
        return initializeMediaVo;
    }
}
