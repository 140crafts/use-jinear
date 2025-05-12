package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectMilestoneManager;
import co.jinear.core.model.request.project.InitializeMilestoneRequest;
import co.jinear.core.model.request.project.MilestoneUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/milestone")
public class ProjectMilestoneController {

    private final ProjectMilestoneManager projectMilestoneManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initialize(@Valid @RequestBody InitializeMilestoneRequest initializeMilestoneRequest) {
        return projectMilestoneManager.initialize(initializeMilestoneRequest);
    }

    @DeleteMapping("/{milestoneId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse delete(@PathVariable String milestoneId) {
        return projectMilestoneManager.deleteMilestone(milestoneId);
    }

    @PutMapping("/title")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTitle(@Valid @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
        return projectMilestoneManager.updateTitle(milestoneUpdateRequest);
    }

    @PutMapping("/description")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateDescription(@Valid @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
        return projectMilestoneManager.updateDescription(milestoneUpdateRequest);
    }

    @PutMapping("/target-date")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTargetDate(@Valid @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
        return projectMilestoneManager.updateTargetDate(milestoneUpdateRequest);
    }

    @PutMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateOrder(@Valid @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
        return projectMilestoneManager.updateOrder(milestoneUpdateRequest);
    }

    @PutMapping("/state")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateState(@Valid @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
        return projectMilestoneManager.updateState(milestoneUpdateRequest);
    }
}
