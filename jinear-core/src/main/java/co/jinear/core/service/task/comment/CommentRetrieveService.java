package co.jinear.core.service.task.comment;

import co.jinear.core.converter.task.CommentDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.entity.task.Comment;
import co.jinear.core.repository.task.CommentRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentRetrieveService {

    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final AccountRetrieveService accountRetrieveService;

    public Comment retrieveEntity(String commentId) {
        log.info("Retrieve comment entity has started. commentId: {}", commentId);
        return commentRepository.findByCommentIdAndPassiveIdIsNull(commentId)
                .orElseThrow(NotFoundException::new);
    }

    public CommentDto retrieveComment(String commentId) {
        log.info("Retrieve comment has started. commentId: {}", commentId);
        return commentRepository.findByCommentIdAndPassiveIdIsNull(commentId)
                .map(comment -> commentDtoConverter.convert(comment,accountRetrieveService))
                .orElseThrow(NotFoundException::new);
    }

    public boolean checkCommentOwnership(String commentId, String ownerId) {
        return commentRepository.countAllByCommentIdAndOwnerIdAndPassiveIdIsNull(commentId, ownerId) > 0;
    }
}
