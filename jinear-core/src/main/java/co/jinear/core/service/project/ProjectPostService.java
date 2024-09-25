package co.jinear.core.service.project;

import co.jinear.core.converter.project.InitializeProjectPostVoToEntityConverter;
import co.jinear.core.converter.project.ProjectPostDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.ProjectPost;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.project.InitializeProjectPostVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.ProjectPostRepository;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private static final int PAGE_SIZE = 25;

    private final ProjectPostRepository projectPostRepository;
    private final InitializeProjectPostVoToEntityConverter initializeProjectPostVoToEntityConverter;
    private final RichTextInitializeService richTextInitializeService;
    private final MediaOperationService mediaOperationService;
    private final ProjectPostDtoConverter projectPostDtoConverter;
    private final PassiveService passiveService;

    @Transactional
    public void initialize(InitializeProjectPostVo initializeProjectPostVo) {
        log.info("Initialize project post has started. initializeProjectPostVo: {}", initializeProjectPostVo);
        ProjectPost projectPost = initializeProjectPostVoToEntityConverter.convert(initializeProjectPostVo);
        ProjectPost saved = projectPostRepository.save(projectPost);
        initializeBody(initializeProjectPostVo, saved);
        initializeFilesIfExists(initializeProjectPostVo, saved);
    }

    public Page<ProjectPostDto> retrieveFeedPosts(String projectId, int page) {
        log.info("Retrieve feed posts has started. projectId: {}, page: {}", projectId, page);
        return projectPostRepository.findAllByProjectIdAndPassiveIdIsNullOrderByCreatedDateDesc(projectId, PageRequest.of(page, PAGE_SIZE))
                .map(projectPostDtoConverter::convert);
    }

    public ProjectPostDto retrievePost(String projectId, String projectPostId) {
        log.info("Retrieve feed post has started. projectId: {}, projectPostId: {}", projectId, projectPostId);
        return projectPostRepository.findByProjectIdAndProjectPostIdAndPassiveIdIsNull(projectId, projectPostId)
                .map(projectPostDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public String deletePost(String projectId, String projectPostId) {
        log.info("Delete feed post has started. projectId: {}, projectPostId: {}", projectId, projectPostId);
        ProjectPost projectPost = projectPostRepository.findByProjectIdAndProjectPostIdAndPassiveIdIsNull(projectId, projectPostId)
                .orElseThrow(NotFoundException::new);
        String passiveId = passiveService.createUserActionPassive();
        projectPost.setPassiveId(passiveId);
        projectPostRepository.save(projectPost);
        return passiveId;
    }

    private void initializeBody(InitializeProjectPostVo initializeProjectPostVo, ProjectPost saved) {
        Optional.of(initializeProjectPostVo)
                .map(InitializeProjectPostVo::getBody)
                .map(body -> mapInfoRichTextVo(saved.getProjectPostId(), body))
                .map(richTextInitializeService::initializeRichText)
                .map(RichTextDto::getRichTextId)
                .ifPresent(bodyRichTextId -> {
                    saved.setPostBodyRichTextId(bodyRichTextId);
                    projectPostRepository.save(saved);
                });
    }

    private InitializeRichTextVo mapInfoRichTextVo(String postId, String body) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(postId);
        initializeRichTextVo.setValue(body);
        initializeRichTextVo.setType(RichTextType.PROJECT_FEED_INFO);
        return initializeRichTextVo;
    }

    private void initializeFilesIfExists(InitializeProjectPostVo initializeProjectPostVo, ProjectPost saved) {
        Optional.of(initializeProjectPostVo)
                .map(InitializeProjectPostVo::getFiles)
                .map(Collection::stream)
                .ifPresent(fileListStream -> fileListStream
                        .map(file -> mapInitializeMediaVo(initializeProjectPostVo.getAccountId(), saved.getProjectPostId(), file))
                        .forEach(mediaOperationService::initializeMedia)
                );
    }

    private InitializeMediaVo mapInitializeMediaVo(String ownerId, String postId, MultipartFile file) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(ownerId);
        initializeMediaVo.setRelatedObjectId(postId);
        initializeMediaVo.setFile(file);
        initializeMediaVo.setFileType(FileType.PROJECT_POST_FILE);
        initializeMediaVo.setMediaOwnerType(MediaOwnerType.PROJECT_POST);
        initializeMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
        return initializeMediaVo;
    }
}
