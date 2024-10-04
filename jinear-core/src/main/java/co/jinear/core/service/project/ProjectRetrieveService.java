package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.project.PublicProjectDto;
import co.jinear.core.model.entity.project.Project;
import co.jinear.core.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectRetrieveService {

    private final ProjectRepository projectRepository;
    private final ProjectDtoConverter projectDtoConverter;

    public ProjectDto retrieve(String projectId) {
        log.info("Retrieve project has started. projectId: {}", projectId);
        return projectRepository.findByProjectIdAndPassiveIdIsNull(projectId)
                .map(projectDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<ProjectDto> retrieveOptional(String projectId) {
        log.info("Retrieve project optional has started. projectId: {}", projectId);
        return projectRepository.findByProjectIdAndPassiveIdIsNull(projectId)
                .map(projectDtoConverter::convert);
    }

    public Project retrieveEntity(String projectId) {
        log.info("Retrieve project entity has started. projectId: {}", projectId);
        return projectRepository.findByProjectIdAndPassiveIdIsNull(projectId)
                .orElseThrow(NotFoundException::new);
    }

    public PublicProjectDto retrievePublic(String projectId) {
        log.info("Retrieve project public info has started. projectId: {}", projectId);
        return projectRepository.findByProjectIdAndPassiveIdIsNull(projectId)
                .map(projectDtoConverter::convertToPublic)
                .orElseThrow(NotFoundException::new);
    }

    public boolean checkExistsByProjectIdAndWorkspaceId(String projectId, String workspaceId) {
        log.info("Check exists by project id and workspace id has started. projectId: {}, workspaceId: {}", projectId, workspaceId);
        return projectRepository.existsByProjectIdAndWorkspaceIdAndPassiveIdIsNull(projectId, workspaceId);
    }
}
