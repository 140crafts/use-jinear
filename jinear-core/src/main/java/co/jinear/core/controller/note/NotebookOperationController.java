package co.jinear.core.controller.note;

import co.jinear.core.manager.notebook.NotebookOperationManager;
import co.jinear.core.model.request.notebook.NotebookInitializeRequest;
import co.jinear.core.model.request.notebook.NotebookUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.notebook.NotebookInitializeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/notebook/operation")
public class NotebookOperationController {

    private final NotebookOperationManager notebookOperationManager;

    @PostMapping("/workspace/{workspaceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookInitializeResponse initialize(@PathVariable String workspaceId,
                                                 @Valid @RequestBody NotebookInitializeRequest request) {
        return notebookOperationManager.initialize(request, workspaceId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update(@Valid @RequestBody NotebookUpdateRequest request) {
        return notebookOperationManager.update(request);
    }

    @DeleteMapping("/{notebookId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse delete(@PathVariable String notebookId) {
        return notebookOperationManager.delete(notebookId);
    }
}
