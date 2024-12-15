package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectDomainManager;
import co.jinear.core.model.request.project.ProjectDomainInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/domain")
public class ProjectDomainController {

    private final ProjectDomainManager projectDomainManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeProjectDomain(@Valid @RequestBody ProjectDomainInitializeRequest projectDomainInitializeRequest) {
        return projectDomainManager.initialize(projectDomainInitializeRequest);
    }

    @DeleteMapping("/{projectDomainId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteProjectDomain(@PathVariable String projectDomainId) {
        return projectDomainManager.remove(projectDomainId);
    }
}
