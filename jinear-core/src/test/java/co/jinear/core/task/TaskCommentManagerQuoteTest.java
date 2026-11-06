package co.jinear.core.task;

import co.jinear.core.converter.task.InitializeTaskCommentVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.manager.task.TaskCommentManager;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.request.task.InitializeTaskCommentRequest;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.comment.CommentListingService;
import co.jinear.core.service.task.comment.CommentOperationService;
import co.jinear.core.service.task.comment.CommentRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskCommentManagerQuoteTest {

    private final CommentOperationService commentOperationService = Mockito.mock(CommentOperationService.class);
    private final CommentRetrieveService commentRetrieveService = Mockito.mock(CommentRetrieveService.class);
    private final TaskRetrieveService taskRetrieveService = Mockito.mock(TaskRetrieveService.class);
    private final TaskCommentManager taskCommentManager = new TaskCommentManager(
            Mockito.mock(SessionInfoService.class),
            commentOperationService,
            commentRetrieveService,
            Mockito.mock(CommentListingService.class),
            Mockito.mock(WorkspaceValidator.class),
            Mockito.mock(TeamAccessValidator.class),
            taskRetrieveService,
            Mockito.mock(TaskActivityService.class),
            Mockito.mock(InitializeTaskCommentVoConverter.class),
            Mockito.mock(PassiveService.class));

    @BeforeEach
    void setUp() {
        Mockito.when(taskRetrieveService.retrieve("task-1")).thenReturn(new TaskDto());
        Mockito.when(commentOperationService.initializeTaskComment(Mockito.any())).thenReturn(new CommentDto());
    }

    @Test
    void refusesAReplyToACommentOnAnotherTask() {
        Mockito.when(commentRetrieveService.retrieveComment("comment-1")).thenReturn(comment("task-2"));
        InitializeTaskCommentRequest request = request("comment-1");

        assertThatThrownBy(() -> taskCommentManager.initializeComment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("task.comment.quote-comment-different-task");
        Mockito.verifyNoInteractions(commentOperationService);
    }

    @Test
    void acceptsAReplyToACommentOnTheSameTask() {
        Mockito.when(commentRetrieveService.retrieveComment("comment-1")).thenReturn(comment("task-1"));
        InitializeTaskCommentRequest request = request("comment-1");

        taskCommentManager.initializeComment(request);

        Mockito.verify(commentOperationService).initializeTaskComment(Mockito.any());
    }

    @Test
    void acceptsACommentWithoutAQuote() {
        InitializeTaskCommentRequest request = request(null);

        taskCommentManager.initializeComment(request);

        Mockito.verifyNoInteractions(commentRetrieveService);
        Mockito.verify(commentOperationService).initializeTaskComment(Mockito.any());
    }

    @Test
    void refusesAQuoteThatDoesNotExist() {
        Mockito.when(commentRetrieveService.retrieveComment("missing")).thenThrow(new NotFoundException());
        InitializeTaskCommentRequest request = request("missing");

        assertThatThrownBy(() -> taskCommentManager.initializeComment(request))
                .isInstanceOf(NotFoundException.class);
        Mockito.verifyNoInteractions(commentOperationService);
    }

    private InitializeTaskCommentRequest request(String quoteCommentId) {
        InitializeTaskCommentRequest request = new InitializeTaskCommentRequest();
        request.setTaskId("task-1");
        request.setQuoteCommentId(quoteCommentId);
        request.setComment("Reply");
        return request;
    }

    private CommentDto comment(String taskId) {
        CommentDto commentDto = new CommentDto();
        commentDto.setTaskId(taskId);
        return commentDto;
    }
}
