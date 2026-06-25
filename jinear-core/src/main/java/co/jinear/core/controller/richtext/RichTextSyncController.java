package co.jinear.core.controller.richtext;

import co.jinear.core.manager.richtext.RichTextSyncManager;
import co.jinear.core.model.request.richtext.AppendRichTextUpdateRequest;
import co.jinear.core.model.request.richtext.SeedRichTextRequest;
import co.jinear.core.model.request.richtext.SnapshotRichTextRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.richtext.RichTextAppendResponse;
import co.jinear.core.model.response.richtext.RichTextStateResponse;
import co.jinear.core.model.response.richtext.RichTextUpdatesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/rich-text")
public class RichTextSyncController {

    private final RichTextSyncManager richTextSyncManager;

    @GetMapping("/{richTextId}/state")
    @ResponseStatus(HttpStatus.OK)
    public RichTextStateResponse retrieveState(@PathVariable String richTextId) {
        return richTextSyncManager.retrieveState(richTextId);
    }

    @GetMapping("/{richTextId}/updates")
    @ResponseStatus(HttpStatus.OK)
    public RichTextUpdatesResponse retrieveUpdates(@PathVariable String richTextId,
                                                   @RequestParam(required = false, defaultValue = "0") Long since) {
        return richTextSyncManager.retrieveUpdates(richTextId, since);
    }

    @PostMapping("/{richTextId}/updates")
    @ResponseStatus(HttpStatus.CREATED)
    public RichTextAppendResponse appendUpdate(@PathVariable String richTextId,
                                               @Valid @RequestBody AppendRichTextUpdateRequest request) {
        return richTextSyncManager.appendUpdate(richTextId, request);
    }

    @PostMapping("/{richTextId}/seed")
    @ResponseStatus(HttpStatus.OK)
    public RichTextStateResponse seed(@PathVariable String richTextId,
                                      @Valid @RequestBody SeedRichTextRequest request) {
        return richTextSyncManager.seed(richTextId, request);
    }

    @PostMapping("/{richTextId}/snapshot")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse snapshot(@PathVariable String richTextId,
                                 @Valid @RequestBody SnapshotRichTextRequest request) {
        return richTextSyncManager.snapshot(richTextId, request);
    }
}
