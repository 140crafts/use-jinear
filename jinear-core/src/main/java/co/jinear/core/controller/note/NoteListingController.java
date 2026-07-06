package co.jinear.core.controller.note;

import co.jinear.core.manager.note.NoteListingManager;
import co.jinear.core.model.response.note.NotePathAwareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/note")
public class NoteListingController {

    private final NoteListingManager noteListingManager;

    @GetMapping("/notebook/{notebookId}")
    @ResponseStatus(HttpStatus.OK)
    public NotePathAwareResponse list(@PathVariable String notebookId,
                                      @RequestParam(required = false) String noteId,
                                      @RequestParam(required = false, defaultValue = "0") Integer page) {
        return noteListingManager.list(notebookId, noteId, page);
    }
}
