package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectPostManager;
import co.jinear.core.model.request.project.ProjectPostInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @DeleteMapping("/{projectId}/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deletePost(@PathVariable String projectId,
                                   @PathVariable String postId) {
        return projectPostManager.delete(projectId, postId);
    }

    @PostMapping(value = "/{projectId}/{postId}/media", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse addMedia(@PathVariable String projectId,
                                 @PathVariable String postId,
                                 List<MultipartFile> files) {
        return projectPostManager.addMedia(projectId, postId, files);
    }

    @DeleteMapping(value = "/{projectId}/{postId}/media/{mediaId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse removeMedia(@PathVariable String projectId,
                                    @PathVariable String postId,
                                    @PathVariable String mediaId) {
        return projectPostManager.removeMedia(projectId, postId, mediaId);
    }
}
