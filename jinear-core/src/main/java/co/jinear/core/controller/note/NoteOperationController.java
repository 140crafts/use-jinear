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
@RequestMapping(value = "v1/note/workspace/{workspaceId}/operation")
public class NoteOperationController {

    private final NoteOperationManager noteOperationManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteInitializeResponse initialize(@PathVariable String workspaceId,
                                             @Valid @RequestBody NoteInitializeRequest request) {
        return noteOperationManager.initialize(workspaceId, request);
    }

    @PutMapping("/{noteId}/publish/{notebookId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse publish(@PathVariable String workspaceId,
                                @PathVariable String noteId,
                                @PathVariable String notebookId) {
        return noteOperationManager.publish(workspaceId, noteId, notebookId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update(@PathVariable String workspaceId,
                               @Valid @RequestBody NoteUpdateRequest request) {
        return noteOperationManager.update(workspaceId, request);
    }

    @PutMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse move(@PathVariable String workspaceId,
                             @Valid @RequestBody NoteMoveRequest request) {
        return noteOperationManager.move(workspaceId, request);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse delete(@PathVariable String workspaceId,
                               @PathVariable String noteId) {
        return noteOperationManager.delete(workspaceId, noteId);
    }
}
