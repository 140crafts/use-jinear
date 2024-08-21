package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectDtoConverter;
import co.jinear.core.converter.project.ProjectTeamDtoConverter;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.project.ProjectTeamDto;
import co.jinear.core.model.entity.project.ProjectTeam;
import co.jinear.core.repository.project.ProjectTeamRepository;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamListingService {

    private static final int PAGE_SIZE = 25;

    private final ProjectTeamRepository projectTeamRepository;
    private final ProjectDtoConverter projectDtoConverter;
    private final ProjectTeamDtoConverter projectTeamDtoConverter;

    public Page<ProjectDto> retrieveAllByTeamIdOrTeamIdEmpty(List<String> teamIds, int page) {
        log.info("Retrieve all by team id or team id empty has started. page: {}, teamIds: {}", page, NormalizeHelper.listToString(teamIds));
        return projectTeamRepository.findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(teamIds, PageRequest.of(page, PAGE_SIZE))
                .map(ProjectTeam::getProject)
                .map(projectDtoConverter::convert);
    }

    public List<ProjectTeamDto> retrieveAllByTeamIdOrTeamIdEmpty(List<String> teamIds) {
        log.info("Retrieve all by team id has started.teamIds: {}", NormalizeHelper.listToString(teamIds));
        return projectTeamRepository.findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(teamIds)
                .stream()
                .map(projectTeamDtoConverter::convert)
                .toList();
    }
}
