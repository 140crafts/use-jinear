package co.jinear.core.service.project;

import co.jinear.core.converter.project.MilestoneInitializeDtoToInitializeMilestoneVoMapper;
import co.jinear.core.converter.project.ProjectInitializeVoToEntityMapper;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.Project;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.enumtype.project.*;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.model.vo.project.ProjectFeedSettingsInitializeVo;
import co.jinear.core.model.vo.project.ProjectInitializeVo;
import co.jinear.core.model.vo.project.UpdateProjectDatesVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.ProjectRepository;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.project.domain.ProjectDomainOperationService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectOperationService {

    private final ProjectRepository projectRepository;
    private final ProjectInitializeVoToEntityMapper projectInitializeVoToEntityMapper;
    private final RichTextInitializeService richTextInitializeService;
    private final ProjectTeamOperationService projectTeamOperationService;
    private final ProjectRetrieveService projectRetrieveService;
    private final MilestoneOperationService milestoneOperationService;
    private final MilestoneInitializeDtoToInitializeMilestoneVoMapper milestoneInitializeDtoToInitializeMilestoneVoMapper;
    private final ProjectFeedSettingsOperationService projectFeedSettingsOperationService;
    private final ProjectDomainOperationService projectDomainOperationService;
    private final MediaOperationService mediaOperationService;

    @Transactional
    public void initialize(ProjectInitializeVo projectInitializeVo) {
        log.info("Initialize project has started. projectInitializeVo: {}", projectInitializeVo);
        Project saved = mapAndInitialize(projectInitializeVo);
        saved = initializeDescription(projectInitializeVo, saved);
        assignTeams(projectInitializeVo, saved);
        initializeMilestones(projectInitializeVo, saved);
        initializeProjectFeedSettings(saved);
        initializeProjectDomain(saved);
    }

    @Transactional
    public void updateTitle(String projectId, String title) {
        log.info("Update title has started. projectId: {}, title: {}", projectId, title);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        validateProjectIsNotArchived(project);
        project.setTitle(title);
        projectRepository.save(project);
        projectFeedSettingsOperationService.updateAccessKey(projectId, title);
    }

    @Transactional
    public void updateDescription(String projectId, String description) {
        log.info("Update description has started. projectId: {}, description: {}", projectId, description);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        validateProjectIsNotArchived(project);
        InitializeRichTextVo initializeRichTextVo = mapDescriptionInitVo(projectId, description);
        RichTextDto richTextDto = richTextInitializeService.initializeRichText(initializeRichTextVo);
        project.setDescriptionRichTextId(richTextDto.getRichTextId());
        projectRepository.save(project);
    }

    public void updateState(String projectId, ProjectStateType projectState) {
        log.info("Update state has started. projectId: {}, projectState: {}", projectId, projectState);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        validateProjectIsNotArchived(project);
        project.setProjectState(projectState);
        projectRepository.save(project);
    }

    public void updatePriority(String projectId, ProjectPriorityType projectPriority) {
        log.info("Update priority has started. projectId: {}, projectPriority: {}", projectId, projectPriority);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        validateProjectIsNotArchived(project);
        project.setProjectPriority(projectPriority);
        projectRepository.save(project);
    }

    public void updateDates(String projectId, UpdateProjectDatesVo updateProjectDatesVo) {
        log.info("Update dates has started. projectId: {}, updateProjectDatesVo: {}", projectId, updateProjectDatesVo);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        validateProjectIsNotArchived(project);
        if (Boolean.TRUE.equals(updateProjectDatesVo.getUpdateStartDate())) {
            project.setStartDate(updateProjectDatesVo.getStartDate());
        }
        if (Boolean.TRUE.equals(updateProjectDatesVo.getUpdateTargetDate())) {
            project.setTargetDate(updateProjectDatesVo.getTargetDate());
        }
        projectRepository.save(project);
    }

    public void updateLead(String projectId, String workspaceMemberId) {
        log.info("Update lead has started. projectId: {}, workspaceMemberId: {}", projectId, workspaceMemberId);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        validateProjectIsNotArchived(project);
        project.setLeadWorkspaceMemberId(workspaceMemberId);
        projectRepository.save(project);
    }

    public void updateArchived(String projectId, boolean archived) {
        log.info("Update project archived has started. projectId: {}, archived: {}", projectId, archived);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        project.setArchived(archived);
        projectRepository.save(project);
    }

    @Transactional
    public void updateLogo(String projectId, MultipartFile logo) {
        log.info("Update logo has started. projectId: {}", projectId);

        Optional<String> oldLogoMediaIdOptional = Optional.of(projectId)
                .map(projectRetrieveService::retrieve)
                .map(ProjectDto::getLogo)
                .map(MediaDto::getMediaId);

        InitializeMediaVo initializeMediaVo = mapLogoInitializeMediaVo(projectId, logo);
        mediaOperationService.initializeMedia(initializeMediaVo);

        oldLogoMediaIdOptional.ifPresent(mediaId -> {
            RemoveMediaVo removeMediaVo = new RemoveMediaVo();
            removeMediaVo.setMediaId(mediaId);
            mediaOperationService.deleteMedia(removeMediaVo);
        });
    }

    private void validateProjectIsNotArchived(Project project) {
        if (Boolean.TRUE.equals(project.getArchived())) {
            throw new BusinessException("project.archived");
        }
    }

    private Project mapAndInitialize(ProjectInitializeVo projectInitializeVo) {
        Project project = projectInitializeVoToEntityMapper.map(projectInitializeVo);
        return projectRepository.save(project);
    }

    private Project initializeDescription(ProjectInitializeVo projectInitializeVo, Project saved) {
        return Optional.of(projectInitializeVo)
                .map(ProjectInitializeVo::getDescription)
                .map(description -> mapDescriptionInitVo(saved.getProjectId(), description))
                .map(richTextInitializeService::initializeRichText)
                .map(RichTextDto::getRichTextId)
                .map(richTextId -> {
                    saved.setDescriptionRichTextId(richTextId);
                    return projectRepository.save(saved);
                })
                .orElse(saved);
    }

    private void assignTeams(ProjectInitializeVo projectInitializeVo, Project saved) {
        Optional.of(projectInitializeVo)
                .map(ProjectInitializeVo::getTeamIds)
                .ifPresent(teamIds -> projectTeamOperationService.initializeAll(saved.getProjectId(), teamIds));
    }

    private void initializeMilestones(ProjectInitializeVo projectInitializeVo, Project saved) {
        Optional.of(projectInitializeVo)
                .map(ProjectInitializeVo::getMilestones)
                .map(Collection::stream)
                .ifPresent(milestoneInitializeDtoStream ->
                        milestoneInitializeDtoStream
                                .map(milestoneInitializeDto -> milestoneInitializeDtoToInitializeMilestoneVoMapper.map(milestoneInitializeDto, saved.getProjectId()))
                                .forEach(milestoneOperationService::initialize)
                );
    }

    private void initializeProjectFeedSettings(Project saved) {
        ProjectFeedSettingsInitializeVo projectFeedSettingsInitializeVo = new ProjectFeedSettingsInitializeVo();
        projectFeedSettingsInitializeVo.setProjectId(saved.getProjectId());
        projectFeedSettingsInitializeVo.setProjectTitle(saved.getTitle());
        projectFeedSettingsInitializeVo.setProjectFeedAccessType(ProjectFeedAccessType.PRIVATE);
        projectFeedSettingsInitializeVo.setProjectPostInitializeAccessType(ProjectPostInitializeAccessType.PROJECT_TEAM_MEMBERS);
        projectFeedSettingsInitializeVo.setProjectPostCommentPolicyType(ProjectPostCommentPolicyType.WORKSPACE_MEMBERS);

        projectFeedSettingsOperationService.initialize(projectFeedSettingsInitializeVo);
    }

    private void initializeProjectDomain(Project saved) {
        projectDomainOperationService.initializeBaseProjectDomain(saved.getProjectId());
    }

    private InitializeRichTextVo mapDescriptionInitVo(String projectId, String description) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(projectId);
        initializeRichTextVo.setValue(description);
        initializeRichTextVo.setType(RichTextType.PROJECT);
        return initializeRichTextVo;
    }

    private InitializeMediaVo mapLogoInitializeMediaVo(String projectId, MultipartFile logo) {
        InitializeMediaVo initializeMediaVo = new InitializeMediaVo();
        initializeMediaVo.setOwnerId(projectId);
        initializeMediaVo.setRelatedObjectId(projectId);
        initializeMediaVo.setFile(logo);
        initializeMediaVo.setFileType(FileType.PROJECT_LOGO);
        initializeMediaVo.setMediaOwnerType(MediaOwnerType.PROJECT);
        initializeMediaVo.setVisibility(MediaVisibilityType.PUBLIC);
        return initializeMediaVo;
    }
}
