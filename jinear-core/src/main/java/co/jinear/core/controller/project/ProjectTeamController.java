package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectTeamManager;
import co.jinear.core.model.request.project.ProjectTeamOperationRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/team")
public class ProjectTeamController {

    private final ProjectTeamManager projectTeamManager;

    @PutMapping("/update-as")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse updateAs(@Valid @RequestBody ProjectTeamOperationRequest projectTeamOperationRequest) {
        return projectTeamManager.updateAs(projectTeamOperationRequest);
    }
}
