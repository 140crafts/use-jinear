package co.jinear.core.controller;

import co.jinear.core.service.media.MediaOperationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/v1/debug")
@RequiredArgsConstructor
public class DebugController {

    private final MediaOperationService mediaOperationService;


    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity, @RequestParam("file") MultipartFile file) throws Exception {

    }

    @GetMapping
    public Object debug2(HttpServletResponse response) {
        return null;
//        InitializeWaitingMediaVo initializeWaitingMediaVo = new InitializeWaitingMediaVo();
//        initializeWaitingMediaVo.setOwnerId("ownerId");
//        initializeWaitingMediaVo.setRelatedObjectId("relatedObjectId");
//        initializeWaitingMediaVo.setFileType(FileType.TASK_FILE);
//        initializeWaitingMediaVo.setMediaOwnerType(MediaOwnerType.TASK);
//        initializeWaitingMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
//        initializeWaitingMediaVo.setOwnershipStatus(MediaFileOwnershipStatusType.OWNED);
//        initializeWaitingMediaVo.setOriginalName("originalName");
//        initializeWaitingMediaVo.setContentType("image/jpeg");
//        WaitingMediaResultDto waitingMediaResultDto = mediaOperationService.initializeWaitingMediaAndGetPresignedUploadUrl(initializeWaitingMediaVo);
//        log.info("WaitingMediaResultDto: {}", waitingMediaResultDto);
//        return waitingMediaResultDto;
    }
}
