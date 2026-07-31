package co.jinear.core.controller.note;

import co.jinear.core.manager.notetag.NoteTagManager;
import co.jinear.core.model.request.notetag.AssignNoteTagRequest;
import co.jinear.core.model.request.notetag.NoteTagInitializeRequest;
import co.jinear.core.model.request.notetag.NoteTagUpdateRequest;
import co.jinear.core.model.request.notetag.UpdateNoteTagAssignmentsRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.notetag.NoteTagInitializeResponse;
import co.jinear.core.model.response.notetag.NoteTagListingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/note/tag")
public class NoteTagController {

    private final NoteTagManager noteTagManager;

    @GetMapping("/notebook/{notebookId}")
    @ResponseStatus(HttpStatus.OK)
    public NoteTagListingResponse list(@PathVariable String notebookId) {
        return noteTagManager.list(notebookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteTagInitializeResponse create(@Valid @RequestBody NoteTagInitializeRequest request) {
        return noteTagManager.create(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update(@Valid @RequestBody NoteTagUpdateRequest request) {
        return noteTagManager.update(request);
    }

    @DeleteMapping("/{noteTagId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse delete(@PathVariable String noteTagId) {
        return noteTagManager.delete(noteTagId);
    }

    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse assign(@Valid @RequestBody AssignNoteTagRequest request) {
        return noteTagManager.assign(request);
    }

    @PostMapping("/assignments")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateAssignments(@Valid @RequestBody UpdateNoteTagAssignmentsRequest request) {
        return noteTagManager.updateAssignments(request);
    }

    @PostMapping("/unassign")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse unassign(@Valid @RequestBody AssignNoteTagRequest request) {
        return noteTagManager.unassign(request);
    }
}
