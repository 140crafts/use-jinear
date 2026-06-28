package co.jinear.core.controller.note;

import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.model.response.notebook.NotebookListingResponse;
import co.jinear.core.model.response.notebook.NotebookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/notebook")
public class NotebookListingController {

    private final NotebookListingManager notebookListingManager;

    @GetMapping("/{notebookId}")
    @ResponseStatus(HttpStatus.OK)
    public NotebookResponse retrieve(@PathVariable String notebookId) {
        return notebookListingManager.retrieve(notebookId);
    }

    @GetMapping("/workspace/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public NotebookListingResponse listWorkspaceNotebooks(@PathVariable String workspaceId,
                                                          @RequestParam(required = false, defaultValue = "0") Integer page) {
        return notebookListingManager.listWorkspaceNotebooks(workspaceId, page);
    }
}
