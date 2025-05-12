package co.jinear.core.manager.task;

import co.jinear.core.converter.task.InitializeTaskCommentVoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.InitializeTaskCommentRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.PaginatedTaskCommentResponse;
import co.jinear.core.model.vo.task.InitializeTaskCommentVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.comment.CommentListingService;
import co.jinear.core.service.task.comment.CommentOperationService;
import co.jinear.core.service.task.comment.CommentRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskCommentManager {

    private final SessionInfoService sessionInfoService;
    private final CommentOperationService commentOperationService;
    private final CommentRetrieveService commentRetrieveService;
    private final CommentListingService commentListingService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskActivityService taskActivityService;
    private final InitializeTaskCommentVoConverter initializeTaskCommentVoConverter;
    private final PassiveService passiveService;

    public BaseResponse initializeComment(InitializeTaskCommentRequest initializeTaskCommentRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDto = validateAccess(initializeTaskCommentRequest.getTaskId(), currentAccountId);
        validateQuotedCommentAccessIfPresent(initializeTaskCommentRequest, currentAccountId);
        log.info("Initialize comment has started. currentAccountId: {}", currentAccountId);
        InitializeTaskCommentVo initializeTaskCommentVo = initializeTaskCommentVoConverter.convert(initializeTaskCommentRequest, currentAccountId);
        CommentDto commentDto = commentOperationService.initializeTaskComment(initializeTaskCommentVo);
        taskActivityService.initializeNewCommentActivity(currentAccountId, currentAccountSessionId, commentDto.getCommentId(), taskDto);
        return new BaseResponse();
    }

    public BaseResponse deleteComment(String commentId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateCommentOwnership(commentId, currentAccountId);
        log.info("Delete comment has started. currentAccountId: {}", currentAccountId);
        String passiveId = commentOperationService.deleteComment(commentId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    public PaginatedTaskCommentResponse retrieveTaskComments(String taskId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(taskId, currentAccountId);
        log.info("Retrieve task comments has started. currentAccountId: {}, page: {}", currentAccountId, page);
        Page<CommentDto> commentDtoPage = commentListingService.retrieveTaskComments(taskId, page);
        return mapResponse(commentDtoPage);
    }

    private PaginatedTaskCommentResponse mapResponse(Page<CommentDto> commentDtoPage) {
        PaginatedTaskCommentResponse paginatedTaskCommentResponse = new PaginatedTaskCommentResponse();
        paginatedTaskCommentResponse.setCommentsPage(new PageDto<>(commentDtoPage));
        return paginatedTaskCommentResponse;
    }

    private void validateCommentOwnership(String commentId, String currentAccountId) {
        if (Boolean.FALSE.equals(commentRetrieveService.checkCommentOwnership(commentId, currentAccountId))) {
            throw new NoAccessException();
        }
    }

    private void validateQuotedCommentAccessIfPresent(InitializeTaskCommentRequest initializeTaskCommentRequest, String currentAccountId) {
        if(Objects.nonNull(initializeTaskCommentRequest.getQuoteCommentId())){
            CommentDto commentDto = commentRetrieveService.retrieveComment(initializeTaskCommentRequest.getQuoteCommentId());
            validateAccess(commentDto.getTaskId(), currentAccountId);
        }
    }

    private TaskDto validateAccess(String taskId, String currentAccountId) {
        TaskDto taskDto = taskRetrieveService.retrieve(taskId);
        workspaceValidator.validateHasAccess(currentAccountId, taskDto.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(currentAccountId, taskDto.getTeamId());
        return taskDto;
    }
}
