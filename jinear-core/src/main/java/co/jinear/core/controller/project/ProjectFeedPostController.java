package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectPostManager;
import co.jinear.core.model.request.project.ProjectPostInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/feed/post")
public class ProjectFeedPostController {

    private final ProjectPostManager projectPostManager;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initialize(@Valid ProjectPostInitializeRequest projectPostInitializeRequest) {
        return projectPostManager.initialize(projectPostInitializeRequest);
    }
}
