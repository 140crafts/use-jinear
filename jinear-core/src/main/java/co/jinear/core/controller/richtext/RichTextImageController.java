package co.jinear.core.controller.richtext;

import co.jinear.core.manager.richtext.RichTextImageManager;
import co.jinear.core.model.response.richtext.RichTextTempImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/v1/rich-text/image")
@RequiredArgsConstructor
public class RichTextImageController {

    private final RichTextImageManager richTextImageManager;

    @PostMapping(value = "/{workspaceId}/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public RichTextTempImageResponse upload(@PathVariable String workspaceId,
                                            MultipartFile file) {
        return richTextImageManager.upload(workspaceId, file);
    }
}
