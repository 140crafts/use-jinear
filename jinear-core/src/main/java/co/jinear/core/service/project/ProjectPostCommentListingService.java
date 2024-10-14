package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectPostCommentDtoConverter;
import co.jinear.core.model.dto.project.ProjectPostCommentDto;
import co.jinear.core.repository.project.ProjectPostCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectPostCommentListingService {

    private static final int PAGE_SIZE = 25;

    private final ProjectPostCommentRepository projectPostCommentRepository;
    private final ProjectPostCommentDtoConverter projectPostCommentDtoConverter;

    public Page<ProjectPostCommentDto> retrievePostComments(String projectPostId, int page) {
        return projectPostCommentRepository.findAllByProjectPostIdOrderByCreatedDateAsc(projectPostId, PageRequest.of(page, PAGE_SIZE))
                .map(projectPostCommentDtoConverter::convert);
    }

    public Long commentCount(String projectPostId) {
        log.info("Comment count has started. projectPostId: {}", projectPostId);
        return projectPostCommentRepository.countAllByProjectPostIdAndPassiveIdIsNull(projectPostId);
    }
}
