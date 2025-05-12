package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectManager;
import co.jinear.core.model.request.project.*;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/operation")
public class ProjectOperationController {

    private final ProjectManager projectManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initialize(@Valid @RequestBody ProjectInitializeRequest projectInitializeRequest) {
        return projectManager.initializeProject(projectInitializeRequest);
    }

    @PutMapping("/{projectId}/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitle(@PathVariable String projectId,
                                    @Valid @RequestBody ProjectTitleUpdateRequest projectTitleUpdateRequest) {
        return projectManager.updateTitle(projectId, projectTitleUpdateRequest);
    }

    @PutMapping("/{projectId}/description")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDescription(@PathVariable String projectId,
                                          @Valid @RequestBody ProjectDescriptionUpdateRequest projectDescriptionUpdateRequest) {
        return projectManager.updateDescription(projectId, projectDescriptionUpdateRequest);
    }

    @PutMapping("/{projectId}/state")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateState(@PathVariable String projectId,
                                    @Valid @RequestBody ProjectStateUpdateRequest projectStateUpdateRequest) {
        return projectManager.updateState(projectId, projectStateUpdateRequest);
    }

    @PutMapping("/{projectId}/priority")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updatePriority(@PathVariable String projectId,
                                       @Valid @RequestBody ProjectPriorityUpdateRequest projectPriorityUpdateRequest) {
        return projectManager.updatePriority(projectId, projectPriorityUpdateRequest);
    }

    @PutMapping("/{projectId}/dates")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDates(@PathVariable String projectId,
                                    @Valid @RequestBody ProjectDatesUpdateRequest projectDatesUpdateRequest) {
        return projectManager.updateDates(projectId, projectDatesUpdateRequest);
    }

    @PutMapping("/{projectId}/lead")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateLead(@PathVariable String projectId,
                                   @Valid @RequestBody ProjectUpdateLeadRequest projectUpdateLeadRequest) {
        return projectManager.updateLead(projectId, projectUpdateLeadRequest);
    }

    @PutMapping(value = "/{projectId}/logo", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse updateLogo(@PathVariable String projectId,
                                   MultipartFile file) {
        return projectManager.updateLogo(projectId, file);
    }

    @PutMapping("/{projectId}/archived")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateArchived(@PathVariable String projectId,
                                       @Valid @RequestBody ProjectUpdateArchivedRequest projectUpdateArchivedRequest) {
        return projectManager.updateArchived(projectId, projectUpdateArchivedRequest);
    }
}
