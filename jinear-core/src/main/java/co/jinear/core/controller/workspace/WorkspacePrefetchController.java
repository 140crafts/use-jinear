package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspacePrefetchManager;
import co.jinear.core.model.request.workspace.WorkspacePrefetchRequest;
import co.jinear.core.model.response.workspace.WorkspacePrefetchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/workspace/prefetch")
public class WorkspacePrefetchController {

    private final WorkspacePrefetchManager workspacePrefetchManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public WorkspacePrefetchResponse prefetch(@Valid @RequestBody WorkspacePrefetchRequest workspacePrefetchRequest) {
        return workspacePrefetchManager.prefetch(workspacePrefetchRequest);
    }
}
