package co.jinear.core.service.task.media;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskMediaOperationService {

    private static final Long PUBLIC_WINDOW_IN_SECONDS = 10L;

    private final MediaOperationService mediaOperationService;
    private final MediaRetrieveService mediaRetrieveService;

    public AccessibleMediaDto upload(String ownerId, TaskDto taskDto, MultipartFile file) {
        log.info("Upload task file has started. ownerId: {}, taskId: {}, originalName: {}, size: {}", ownerId, taskDto.getTaskId(), file.getOriginalFilename(), file.getSize());
        InitializeMediaVo initializeMediaVo = mapInitializeMediaVo(ownerId, taskDto.getTaskId(), file);
        return mediaOperationService.initializeMedia(initializeMediaVo);
    }

    public AccessibleMediaDto delete(String responsibleAccountId, String mediaId, String taskId) {
        log.info("Delete task media has started. responsibleAccountId: {}, mediaId: {}, taskId: {}", responsibleAccountId, mediaId, taskId);
        MediaDto mediaDto = retrieveMedia(mediaId, taskId);
        return delete(responsibleAccountId, mediaDto);
    }

    private AccessibleMediaDto delete(String responsibleAccountId, MediaDto mediaDto) {
        RemoveMediaVo removeMediaVo = new RemoveMediaVo();
        removeMediaVo.setMediaId(mediaDto.getMediaId());
        removeMediaVo.setResponsibleAccountId(responsibleAccountId);
        return mediaOperationService.deleteMedia(removeMediaVo);
    }

    private MediaDto retrieveMedia(String mediaId, String taskId) {
        log.info("Retrieve media with taskId and media id has started. mediaId: {}, taskId: {}", mediaId, taskId);
        return mediaRetrieveService.retrieveMediaWithMediaIdAndRelatedObjectId(mediaId, taskId);
    }

    private InitializeMediaVo mapInitializeMediaVo(String ownerId, String taskId, MultipartFile file) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(ownerId);
        initializeMediaVo.setRelatedObjectId(taskId);
        initializeMediaVo.setFile(file);
        initializeMediaVo.setFileType(FileType.TASK_FILE);
        initializeMediaVo.setMediaOwnerType(MediaOwnerType.TASK);
        initializeMediaVo.setVisibility(MediaVisibilityType.PRIVATE);
        return initializeMediaVo;
    }

    public void updateMediaAsTemporaryPublic(String mediaId) {
        log.info("Update media as public has started.");
        ZonedDateTime publicUntil = ZonedDateTime.now().plusSeconds(PUBLIC_WINDOW_IN_SECONDS);
        mediaOperationService.updateMediaAsTemporaryPublic(mediaId, publicUntil);
    }

    public void updateMediaAsPrivate(String mediaId) {
        log.info("Update media as private has started.");
        mediaOperationService.updateMediaAsPrivate(mediaId);
    }
}
