package co.jinear.core.controller.note;

import co.jinear.core.manager.note.NoteOperationManager;
import co.jinear.core.model.request.note.NoteInitializeRequest;
import co.jinear.core.model.request.note.NoteMoveRequest;
import co.jinear.core.model.request.note.NoteUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.note.NoteInitializeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/note/operation")
public class NoteOperationController {

    private final NoteOperationManager noteOperationManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteInitializeResponse initialize(@Valid @RequestBody NoteInitializeRequest request) {
        return noteOperationManager.initialize(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update(@Valid @RequestBody NoteUpdateRequest request) {
        return noteOperationManager.update(request);
    }

    @PutMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse move(@Valid @RequestBody NoteMoveRequest request) {
        return noteOperationManager.move(request);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse delete(@PathVariable String noteId) {
        return noteOperationManager.delete(noteId);
    }
}
