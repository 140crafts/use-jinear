package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskMediaManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskMediaResponse;
import co.jinear.core.model.response.task.TaskPaginatedMediaResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/media")
public class TaskMediaController {

    private final TaskMediaManager taskMediaManager;

    @GetMapping(value = "/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskMediaResponse retrieveTaskMediaList(@PathVariable String taskId) {
        return taskMediaManager.retrieveTaskMediaList(taskId);
    }

    @GetMapping(value = "/from-team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskPaginatedMediaResponse retrieveTaskMediaListFromTeam(@PathVariable String teamId,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskMediaManager.retrieveTaskMediaListFromTeam(teamId, page);
    }

    @PostMapping(value = "/{taskId}/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse uploadTaskMedia(@PathVariable String taskId,
                                        @RequestParam("file") MultipartFile file) {
        return taskMediaManager.uploadTaskMedia(taskId, file);
    }

    @DeleteMapping(value = "/{taskId}/delete/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTaskMedia(@PathVariable String taskId,
                                        @PathVariable String mediaId) {
        return taskMediaManager.deleteTaskMedia(taskId, mediaId);
    }

    @GetMapping(value = "/{taskId}/download/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public void downloadTaskMedia(@PathVariable String taskId,
                                  @PathVariable String mediaId,
                                  HttpServletResponse response) throws IOException {
        taskMediaManager.downloadTaskMedia(response, taskId, mediaId);
    }
}
