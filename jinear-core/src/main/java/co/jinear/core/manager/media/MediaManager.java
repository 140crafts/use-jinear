package co.jinear.core.manager.media;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.team.member.TeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static co.jinear.core.model.enumtype.team.TeamAccountRoleType.ADMIN;
import static co.jinear.core.model.enumtype.team.TeamAccountRoleType.OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaManager {

    private final MediaOperationService mediaOperationService;
    private final TeamMemberService teamMemberService;
    private final SessionInfoService sessionInfoService;

    public BaseResponse changeAccountProfilePicture(MultipartFile file) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Change profile picture has started. accountId: {}", currentAccountId);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(file, currentAccountId, currentAccountId, MediaOwnerType.USER);
        mediaOperationService.changeProfilePicture(initializeMediaVo);
        log.info("Change profile picture has finished. accountId: {}", currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse changeTeamProfilePicture(MultipartFile file, String teamId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Change team picture has started. accountId: {}, teamId: {}", currentAccountId, teamId);
        InitializeMediaVo initializeMediaVo = mapInitializeVo(file, currentAccountId, teamId, MediaOwnerType.USER);
        teamMemberService.validateAccountHasRoleInTeam(currentAccountId, teamId, List.of(OWNER, ADMIN));
        mediaOperationService.changeProfilePicture(initializeMediaVo);
        log.info("Change team picture has finished. accountId: {}, teamId: {}", currentAccountId, teamId);
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
