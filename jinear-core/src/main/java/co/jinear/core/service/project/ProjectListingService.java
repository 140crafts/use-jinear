package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectDtoConverter;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectListingService {

    private static final int PAGE_SIZE = 50;

    private final ProjectRepository projectRepository;
    private final ProjectDtoConverter projectDtoConverter;

    public Page<ProjectDto> retrieveWorkspaceProjects(String workspaceId, int page) {
        log.info("Retrieve workspace projects has started. workspaceId: {}, page: {}", workspaceId, page);
        return projectRepository.findAllByWorkspaceIdAndArchivedAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, Boolean.FALSE, PageRequest.of(page, PAGE_SIZE))
                .map(projectDtoConverter::convert);
    }

    public Page<ProjectDto> retrieveArchivedWorkspaceProjects(String workspaceId, int page) {
        log.info("Retrieve archived workspace projects has started. workspaceId: {}, page: {}", workspaceId, page);
        return projectRepository.findAllByWorkspaceIdAndArchivedAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, Boolean.TRUE, PageRequest.of(page, PAGE_SIZE))
                .map(projectDtoConverter::convert);
    }
}
