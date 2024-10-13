package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectPostCommentDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.ProjectPostCommentDto;
import co.jinear.core.model.entity.project.ProjectPostComment;
import co.jinear.core.repository.project.ProjectPostCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectPostCommentRetrieveService {

    private final ProjectPostCommentRepository projectPostCommentRepository;
    private final ProjectPostCommentDtoConverter projectPostCommentDtoConverter;

    public ProjectPostComment retrieveEntity(String projectPostCommentId) {
        log.info("Retrieve entity has started. projectPostCommentId: {}", projectPostCommentId);
        return projectPostCommentRepository.findByProjectPostCommentIdAndPassiveIdIsNull(projectPostCommentId)
                .orElseThrow(NotFoundException::new);
    }

    public ProjectPostCommentDto retrieve(String projectPostCommentId) {
        log.info("Retrieve has started. projectPostCommentId: {}", projectPostCommentId);
        return projectPostCommentRepository.findByProjectPostCommentIdAndPassiveIdIsNull(projectPostCommentId)
                .map(projectPostCommentDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }
}
