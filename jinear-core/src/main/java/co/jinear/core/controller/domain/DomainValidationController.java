package co.jinear.core.controller.domain;

import co.jinear.core.manager.project.ProjectDomainManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/domain")
public class DomainValidationController {

    private final ProjectDomainManager projectDomainManager;

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse validateDomain(@RequestParam String domain) {
        return projectDomainManager.validateIsActive(domain);
    }
}
