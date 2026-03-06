package co.jinear.core.manager.media;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.request.media.InternalMediaInitializeFromUrlRequest;
import co.jinear.core.model.request.media.InternalMediaInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.media.InternalMediaInitializeResponse;
import co.jinear.core.model.vo.media.BaseInitializeMediaVo;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalMediaManager {

    private final MediaOperationService mediaOperationService;
    private final MediaRetrieveService mediaRetrieveService;

    public InternalMediaInitializeResponse initializeMedia(MultipartFile file, InternalMediaInitializeRequest request) {
        log.info("Internal initialize media has started. request: {}", request);
        InitializeMediaVo vo = mapToInitializeMediaVo(request);
        vo.setFile(file);
        AccessibleMediaDto result = mediaOperationService.initializeMedia(vo);
        return mapResponse(result);
    }

    public InternalMediaInitializeResponse initializeMediaFromUrl(InternalMediaInitializeFromUrlRequest request) {
        log.info("Internal initialize media from URL has started. request: {}", request);
        try {
            URL url = new URL(request.getUrl());
            BaseInitializeMediaVo baseVo = mapToBaseVo(request);
            AccessibleMediaDto result = mediaOperationService.initializeMediaFromUrl(baseVo, url);
            return mapResponse(result);
        } catch (MalformedURLException e) {
            log.error("Invalid URL: {}", request.getUrl(), e);
            throw new IllegalArgumentException("Invalid URL: " + request.getUrl(), e);
        }
    }

    public InternalMediaInitializeResponse retrieveMedia(String mediaId, String relatedObjectId) {
        log.info("Internal retrieve media has started. mediaId: {}, relatedObjectId: {}", mediaId, relatedObjectId);
        AccessibleMediaDto result = mediaRetrieveService.retrieveAccessibleMediaWithRelatedObjectIdAndFileTypeOptional(relatedObjectId, null)
                .orElse(null);
        return mapResponse(result);
    }

    public BaseResponse deleteMedia(String mediaId, String responsibleAccountId) {
        log.info("Internal delete media has started. mediaId: {}, responsibleAccountId: {}", mediaId, responsibleAccountId);
        RemoveMediaVo removeMediaVo = new RemoveMediaVo(responsibleAccountId, mediaId);
        mediaOperationService.deleteMedia(removeMediaVo);
        return new BaseResponse();
    }

    private BaseInitializeMediaVo mapToBaseVo(InternalMediaInitializeRequest request) {
        BaseInitializeMediaVo vo = new BaseInitializeMediaVo();
        vo.setOwnerId(request.getOwnerId());
        vo.setRelatedObjectId(request.getRelatedObjectId());
        vo.setFileType(request.getFileType());
        vo.setMediaOwnerType(request.getMediaOwnerType());
        vo.setVisibility(request.getVisibility());
        vo.setOwnershipStatus(request.getOwnershipStatus());
        return vo;
    }

    private InitializeMediaVo mapToInitializeMediaVo(InternalMediaInitializeRequest request) {
        InitializeMediaVo vo = new InitializeMediaVo();
        vo.setOwnerId(request.getOwnerId());
        vo.setRelatedObjectId(request.getRelatedObjectId());
        vo.setFileType(request.getFileType());
        vo.setMediaOwnerType(request.getMediaOwnerType());
        vo.setVisibility(request.getVisibility());
        vo.setOwnershipStatus(request.getOwnershipStatus());
        return vo;
    }

    private InternalMediaInitializeResponse mapResponse(AccessibleMediaDto mediaDto) {
        InternalMediaInitializeResponse response = new InternalMediaInitializeResponse();
        response.setMedia(mediaDto);
        return response;
    }
}
