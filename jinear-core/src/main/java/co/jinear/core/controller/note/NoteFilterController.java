package co.jinear.core.controller.note;

import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.note.TaskLinkingNoteManager;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.response.note.TaskLinkingNoteListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/note")
public class NoteFilterController {

    private final NoteFilterManager noteFilterManager;
    private final TaskLinkingNoteManager taskLinkingNoteManager;

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public NotePaginatedResponse filter(@Valid @RequestBody NoteFilterRequest noteFilterRequest) {
        return noteFilterManager.filter(noteFilterRequest);
    }

    @GetMapping("/linking-task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskLinkingNoteListResponse retrieveNotesLinkingTask(@PathVariable String taskId) {
        return taskLinkingNoteManager.retrieveNotesLinkingTask(taskId);
    }
}
