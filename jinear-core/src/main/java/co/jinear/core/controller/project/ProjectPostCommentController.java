package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectPostCommentManager;
import co.jinear.core.model.request.project.ProjectPostAddCommentRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.project.ProjectPostPaginatedCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/post/comment")
public class ProjectPostCommentController {

    private final ProjectPostCommentManager projectPostCommentManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse addComment(@Valid @RequestBody ProjectPostAddCommentRequest projectPostAddCommentRequest) {
        return projectPostCommentManager.addComment(projectPostAddCommentRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteComment(@PathVariable String commentId) {
        return projectPostCommentManager.deleteComment(commentId);
    }

    @GetMapping("/list/project/{projectId}/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectPostPaginatedCommentResponse listComments(@PathVariable String postId,
                                                            @PathVariable String projectId,
                                                            @RequestParam(required = false, defaultValue = "0") Integer page) {
        return projectPostCommentManager.listComments(postId, projectId, page);
    }
}
