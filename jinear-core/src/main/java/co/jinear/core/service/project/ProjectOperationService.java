package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectInitializeVoToEntityMapper;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.Project;
import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.enumtype.project.ProjectStateType;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.project.ProjectInitializeVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.ProjectRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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

    @Transactional
    public void initialize(ProjectInitializeVo projectInitializeVo) {
        log.info("Initialize project has started. projectInitializeVo: {}", projectInitializeVo);
        Project saved = mapAndInitialize(projectInitializeVo);
        saved = initializeDescription(projectInitializeVo, saved);
        assignTeams(projectInitializeVo, saved);
    }

    public void updateTitle(String projectId, String title) {
        log.info("Update title has started. projectId: {}, title: {}", projectId, title);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        project.setTitle(title);
        projectRepository.save(project);
    }

    @Transactional
    public void updateDescription(String projectId, String description) {
        log.info("Update description has started. projectId: {}, description: {}", projectId, description);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        InitializeRichTextVo initializeRichTextVo = mapDescriptionInitVo(projectId, description);
        RichTextDto richTextDto = richTextInitializeService.initializeRichText(initializeRichTextVo);
        project.setDescriptionRichTextId(richTextDto.getRichTextId());
        projectRepository.save(project);
    }

    public void updateState(String projectId, ProjectStateType projectState) {
        log.info("Update state has started. projectId: {}, projectState: {}", projectId, projectState);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        project.setProjectState(projectState);
        projectRepository.save(project);
    }

    public void updatePriority(String projectId, ProjectPriorityType projectPriority) {
        log.info("Update priority has started. projectId: {}, projectPriority: {}", projectId, projectPriority);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        project.setProjectPriority(projectPriority);
        projectRepository.save(project);
    }

    public void updateDates(String projectId, ZonedDateTime startDate, ZonedDateTime targetDate) {
        log.info("Update dates has started. projectId: {}, startDate: {}, targetDate: {}", projectId, startDate, targetDate);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        project.setStartDate(startDate);
        project.setTargetDate(targetDate);
        projectRepository.save(project);
    }

    public void updateLead(String projectId, String workspaceMemberId) {
        log.info("Update lead has started. projectId: {}, workspaceMemberId: {}", projectId, workspaceMemberId);
        Project project = projectRetrieveService.retrieveEntity(projectId);
        project.setLeadWorkspaceMemberId(workspaceMemberId);
        projectRepository.save(project);
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

    private InitializeRichTextVo mapDescriptionInitVo(String projectId, String description) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(projectId);
        initializeRichTextVo.setValue(description);
        initializeRichTextVo.setType(RichTextType.PROJECT);
        return initializeRichTextVo;
    }
}
