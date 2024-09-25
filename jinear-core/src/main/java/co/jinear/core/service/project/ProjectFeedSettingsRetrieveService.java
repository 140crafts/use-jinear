package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectFeedSettingsDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.ProjectFeedSettingsDto;
import co.jinear.core.model.entity.project.ProjectFeedSettings;
import co.jinear.core.repository.project.ProjectFeedSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFeedSettingsRetrieveService {

    private final ProjectFeedSettingsRepository projectFeedSettingsRepository;
    private final ProjectFeedSettingsDtoConverter projectFeedSettingsDtoConverter;

    public ProjectFeedSettingsDto retrieve(String projectId) {
        log.info("Retrieve project feed settings has started. projectId: {}", projectId);
        return projectFeedSettingsRepository.findByProjectIdAndPassiveIdIsNull(projectId)
                .map(projectFeedSettingsDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public ProjectFeedSettings retrieveEntityWithProjectId(String projectId) {
        log.info("Retrieve project feed settings entity has started. projectId: {}", projectId);
        return projectFeedSettingsRepository.findByProjectIdAndPassiveIdIsNull(projectId)
                .orElseThrow(NotFoundException::new);
    }
}
