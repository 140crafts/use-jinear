package co.jinear.core.service.task.comment;

import co.jinear.core.converter.task.CommentDtoConverter;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.repository.task.CommentRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentListingService {

    private static final int PAGE_SIZE = 25;

    private final CommentRepository commentRepository;
    private final CommentDtoConverter commentDtoConverter;
    private final AccountRetrieveService accountRetrieveService;

    public Page<CommentDto> retrieveTaskComments(String taskId, int page) {
        return commentRepository.findAllByTaskIdOrderByCreatedDateAsc(taskId, PageRequest.of(page, PAGE_SIZE))
                .map(comment -> commentDtoConverter.convertAndAnonymize(comment, accountRetrieveService));
    }
}
