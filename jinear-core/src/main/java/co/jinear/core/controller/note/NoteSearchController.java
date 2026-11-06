package co.jinear.core.controller.note;

import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/note/workspace/{workspaceId}/search")
public class NoteSearchController {

    private final NoteSearchManager noteSearchManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public NotePaginatedResponse searchNotes(@PathVariable String workspaceId,
                                             @Valid @RequestBody NoteSearchRequest noteSearchRequest,
                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return noteSearchManager.searchNotes(workspaceId, noteSearchRequest, page);
    }
}
