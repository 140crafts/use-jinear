package co.jinear.core.service.project;

import co.jinear.core.converter.project.InitializeProjectPostVoToEntityConverter;
import co.jinear.core.converter.project.ProjectPostDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.ProjectPost;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.model.vo.project.InitializeProjectPostVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.ProjectPostRepository;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
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
import java.util.List;
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
    private final MediaRetrieveService mediaRetrieveService;
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

    public ProjectPostDto retrievePost(String projectPostId) {
        log.info("Retrieve feed post has started.projectPostId: {}", projectPostId);
        return projectPostRepository.findByProjectPostIdAndPassiveIdIsNull(projectPostId)
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

    @Transactional
    public void addMedia(String ownerId, String postId, List<MultipartFile> files) {
        log.info("Add media has started. ownerId: {}, postId: {}", ownerId, postId);
        files.stream().map(file -> mapInitializeMediaVo(ownerId, postId, file))
                .forEach(mediaOperationService::initializeMedia);
    }

    public void validateMediaIdRelatedWithPostAndDelete(String responsibleAccountId, String postId, String mediaId) {
        log.info("Delete media has started. responsibleAccountId: {}, postId: {}, mediaId: {}", responsibleAccountId, postId, mediaId);
        MediaDto mediaDto = retrieveMedia(mediaId, postId);
        deleteMedia(responsibleAccountId, mediaDto);
    }

    private void deleteMedia(String responsibleAccountId, MediaDto mediaDto) {
        log.info("Delete media has started.");
        RemoveMediaVo removeMediaVo = new RemoveMediaVo();
        removeMediaVo.setMediaId(mediaDto.getMediaId());
        removeMediaVo.setResponsibleAccountId(responsibleAccountId);
        mediaOperationService.deleteMedia(removeMediaVo);
    }

    private MediaDto retrieveMedia(String mediaId, String postId) {
        log.info("Retrieve media with post id and media id has started. mediaId: {}, postId: {}", mediaId, postId);
        return mediaRetrieveService.retrieveMediaWithMediaIdAndRelatedObjectId(mediaId, postId);
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
