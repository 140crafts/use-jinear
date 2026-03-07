package co.jinear.core.controller.media;

import co.jinear.core.manager.media.InternalMediaManager;
import co.jinear.core.model.request.media.InternalBatchMediaRetrieveRequest;
import co.jinear.core.model.request.media.InternalMediaInitializeFromUrlRequest;
import co.jinear.core.model.request.media.InternalMediaInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.media.InternalBatchMediaRetrieveResponse;
import co.jinear.core.model.response.media.InternalMediaInitializeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/internal/media")
public class InternalMediaController {

    private final InternalMediaManager internalMediaManager;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public InternalMediaInitializeResponse uploadMedia(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") InternalMediaInitializeRequest metadata) {
        return internalMediaManager.initializeMedia(file, metadata);
    }

    @PostMapping("/upload-from-url")
    @ResponseStatus(HttpStatus.OK)
    public InternalMediaInitializeResponse uploadMediaFromUrl(
            @RequestBody InternalMediaInitializeFromUrlRequest request) {
        return internalMediaManager.initializeMediaFromUrl(request);
    }

    @GetMapping("/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public InternalMediaInitializeResponse retrieveMedia(
            @PathVariable String mediaId,
            @RequestParam String relatedObjectId) {
        return internalMediaManager.retrieveMedia(mediaId, relatedObjectId);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    public InternalBatchMediaRetrieveResponse retrieveMediaBatch(
            @RequestBody InternalBatchMediaRetrieveRequest request) {
        return internalMediaManager.retrieveMediaBatch(request);
    }

    @DeleteMapping("/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteMedia(
            @PathVariable String mediaId,
            @RequestParam String responsibleAccountId) {
        return internalMediaManager.deleteMedia(mediaId, responsibleAccountId);
    }
}
