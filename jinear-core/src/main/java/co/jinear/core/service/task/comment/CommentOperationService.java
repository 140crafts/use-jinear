package co.jinear.core.service.task.comment;

import co.jinear.core.converter.task.CommentDtoConverter;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.entity.task.Comment;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.task.InitializeTaskCommentVo;
import co.jinear.core.repository.task.CommentRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.task.TaskSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentOperationService {

    private final CommentRepository commentRepository;
    private final RichTextInitializeService richTextInitializeService;
    private final CommentDtoConverter commentDtoConverter;
    private final CommentRetrieveService commentRetrieveService;
    private final PassiveService passiveService;
    private final AccountRetrieveService accountRetrieveService;
    private final TaskSearchService taskSearchService;

    @Transactional
    public CommentDto initializeTaskComment(InitializeTaskCommentVo initializeTaskCommentVo) {
        log.info("Initialize task comment has started. initializeTaskCommentVo: {}", initializeTaskCommentVo);
        RichTextDto richTextDto = initializeRichText(initializeTaskCommentVo);
        Comment saved = mapAndInitializeComment(initializeTaskCommentVo, richTextDto);
        richTextInitializeService.updateRelatedObjectId(richTextDto.getRichTextId(), saved.getCommentId());
        taskSearchService.refreshTaskFtsMv();
        log.info("Initialize task comment has completed.");
        return commentDtoConverter.convert(saved, accountRetrieveService);
    }

    @Transactional
    public String deleteComment(String commentId) {
        log.info("Delete comment has started. commentId: {}", commentId);
        Comment comment = commentRetrieveService.retrieveEntity(commentId);
        String passiveId = passiveService.createUserActionPassive();
        comment.setPassiveId(passiveId);
        commentRepository.save(comment);
        taskSearchService.refreshTaskFtsMv();
        log.info("Delete comment has completed. commentId: {},passiveId: {}", commentId, passiveId);
        return passiveId;
    }

    private Comment mapAndInitializeComment(InitializeTaskCommentVo initializeTaskCommentVo, RichTextDto richTextDto) {
        Comment comment = new Comment();
        comment.setTaskId(initializeTaskCommentVo.getTaskId());
        comment.setOwnerId(initializeTaskCommentVo.getOwnerId());
        comment.setQuoteCommentId(initializeTaskCommentVo.getQuoteCommentId());
        comment.setRichTextId(richTextDto.getRichTextId());
        return commentRepository.save(comment);
    }

    private RichTextDto initializeRichText(InitializeTaskCommentVo initializeTaskCommentVo) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setValue(initializeTaskCommentVo.getComment());
        initializeRichTextVo.setType(RichTextType.TASK_COMMENT);

        return richTextInitializeService.initializeRichText(initializeRichTextVo);
    }
}
