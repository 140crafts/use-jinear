package co.jinear.core.service.project;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.project.ProjectPostCommentDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.ProjectPostComment;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.project.ProjectPostInitializeVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.ProjectPostCommentRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectPostCommentOperationService {

    private final ProjectPostCommentRepository projectPostCommentRepository;
    private final RichTextInitializeService richTextInitializeService;
    private final PassiveService passiveService;
    private final ProjectPostCommentRetrieveService projectPostCommentRetrieveService;

    @Transactional
    public void initialize(ProjectPostInitializeVo projectPostInitializeVo) {
        log.info("Initialize project post comment has started. projectPostInitializeVo: {}", projectPostInitializeVo);
        validateCurrentCommentsPostAndQuoteCommentsPostAreSame(projectPostInitializeVo);
        RichTextDto richTextDto = initializeRichText(projectPostInitializeVo);
        ProjectPostComment comment = mapAndInitializeComment(projectPostInitializeVo, richTextDto);
        richTextInitializeService.updateRelatedObjectId(richTextDto.getRichTextId(), comment.getProjectPostCommentId());
        log.info("Initialize project post comment has completed.");
    }

    private void validateCurrentCommentsPostAndQuoteCommentsPostAreSame(ProjectPostInitializeVo projectPostInitializeVo) {
        if (StringUtils.isNotBlank(projectPostInitializeVo.getQuoteProjectPostCommentId())) {
            ProjectPostCommentDto quotePostComment = projectPostCommentRetrieveService.retrieve(projectPostInitializeVo.getQuoteProjectPostCommentId());
            String quoteCommentsPostId = quotePostComment.getProjectPostId();
            if (!StringUtils.equalsIgnoreCase(quoteCommentsPostId, projectPostInitializeVo.getProjectPostId())) {
                throw new BusinessException("project.feed.comment.quote-comments-post-is-different-than-current-comments-post");
            }
        }
    }

    @Transactional
    public String deleteComment(String projectPostCommentId) {
        log.info("Delete comment has started. projectPostCommentId: {}", projectPostCommentId);
        ProjectPostComment comment = projectPostCommentRetrieveService.retrieveEntity(projectPostCommentId);
        String passiveId = passiveService.createUserActionPassive();
        comment.setPassiveId(passiveId);
        projectPostCommentRepository.save(comment);
        return passiveId;
    }

    private ProjectPostComment mapAndInitializeComment(ProjectPostInitializeVo projectPostInitializeVo, RichTextDto richTextDto) {
        ProjectPostComment projectPostComment = new ProjectPostComment();
        projectPostComment.setProjectPostId(projectPostInitializeVo.getProjectPostId());
        projectPostComment.setAccountId(projectPostInitializeVo.getAccountId());
        projectPostComment.setCommentBodyRichTextId(richTextDto.getRichTextId());
        projectPostComment.setQuoteProjectPostCommentId(projectPostInitializeVo.getQuoteProjectPostCommentId());
        return projectPostCommentRepository.save(projectPostComment);
    }

    private RichTextDto initializeRichText(ProjectPostInitializeVo projectPostInitializeVo) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setValue(projectPostInitializeVo.getCommentBody());
        initializeRichTextVo.setType(RichTextType.PROJECT_FEED_POST_COMMENT);

        return richTextInitializeService.initializeRichText(initializeRichTextVo);
    }
}
