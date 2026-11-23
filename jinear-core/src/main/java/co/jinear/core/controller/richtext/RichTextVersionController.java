package co.jinear.core.controller.richtext;

import co.jinear.core.manager.richtext.RichTextVersionManager;
import co.jinear.core.model.response.richtext.RichTextVersionPaginatedResponse;
import co.jinear.core.model.response.richtext.RichTextVersionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/rich-text/{richTextId}/versions")
public class RichTextVersionController {

    private final RichTextVersionManager richTextVersionManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RichTextVersionPaginatedResponse retrieveVersions(@PathVariable String richTextId,
                                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return richTextVersionManager.retrieveVersions(richTextId, page);
    }

    @GetMapping("/{richTextVersionId}")
    @ResponseStatus(HttpStatus.OK)
    public RichTextVersionResponse retrieveVersion(@PathVariable String richTextId,
                                                   @PathVariable String richTextVersionId) {
        return richTextVersionManager.retrieveVersion(richTextId, richTextVersionId);
    }
}
