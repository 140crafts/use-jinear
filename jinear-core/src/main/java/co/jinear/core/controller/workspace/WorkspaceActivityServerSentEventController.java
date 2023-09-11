package co.jinear.core.controller.workspace;

import co.jinear.core.manager.workspace.WorkspaceActivitySseManager;
import co.jinear.core.model.response.workspace.WorkspaceActivityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/sse/workspace/activity")
public class WorkspaceActivityServerSentEventController {

    private final WorkspaceActivitySseManager workspaceActivitySseManager;

    @GetMapping(value = "/{workspaceId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ReadOperation(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ServerSentEvent<WorkspaceActivityResponse>> retrieveActivities(@PathVariable String workspaceId) {
        return workspaceActivitySseManager.retrieveWorkspaceLatestActivity(workspaceId);
    }
}
