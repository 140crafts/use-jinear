package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectFeedSettingsManager;
import co.jinear.core.model.request.project.ProjectFeedSettingsOperationRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/feed/settings")
public class ProjectFeedSettingsController {

    private final ProjectFeedSettingsManager projectFeedSettingsManager;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update(@Valid @RequestBody ProjectFeedSettingsOperationRequest projectFeedSettingsOperationRequest) {
        return projectFeedSettingsManager.update(projectFeedSettingsOperationRequest);
    }
}
