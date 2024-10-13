package co.jinear.core.service.project;

import co.jinear.core.converter.project.ProjectFeedAccessKeyMapper;
import co.jinear.core.converter.project.projectFeedSettingsInitializeVoToEntityConverter;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.ProjectFeedSettings;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.project.ProjectFeedSettingsInitializeVo;
import co.jinear.core.model.vo.project.ProjectFeedSettingsOperationVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.ProjectFeedSettingsRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFeedSettingsOperationService {

    private final ProjectFeedSettingsRepository projectFeedSettingsRepository;
    private final projectFeedSettingsInitializeVoToEntityConverter projectFeedSettingsInitializeVoToEntityConverter;
    private final ProjectFeedSettingsRetrieveService projectFeedSettingsRetrieveService;
    private final ProjectFeedAccessKeyMapper projectFeedAccessKeyMapper;
    private final RichTextInitializeService richTextInitializeService;

    public void initialize(ProjectFeedSettingsInitializeVo projectFeedSettingsInitializeVo) {
        log.info("Initialize project feed settings has started. projectFeedSettingsInitializeVo: {}", projectFeedSettingsInitializeVo);
        String accessKey = projectFeedAccessKeyMapper.generateAccessKey(projectFeedSettingsInitializeVo.getProjectId(), projectFeedSettingsInitializeVo.getProjectTitle());
        ProjectFeedSettings projectFeedSettings = projectFeedSettingsInitializeVoToEntityConverter.convert(projectFeedSettingsInitializeVo, accessKey);
        projectFeedSettingsRepository.save(projectFeedSettings);
    }

    @Transactional
    public void update(ProjectFeedSettingsOperationVo projectFeedSettingsOperationVo) {
        log.info("Update project feed settings has started. projectFeedSettingsOperationVo: {}", projectFeedSettingsOperationVo);
        ProjectFeedSettings projectFeedSettings = projectFeedSettingsRetrieveService.retrieveEntityWithProjectId(projectFeedSettingsOperationVo.getProjectId());
        Optional<RichTextDto> richTextDtoOptional = initializeIfInfoProvided(projectFeedSettingsOperationVo, projectFeedSettings);
        mapUpdates(projectFeedSettingsOperationVo, projectFeedSettings, richTextDtoOptional);
        projectFeedSettingsRepository.save(projectFeedSettings);
    }

    @Transactional
    public void updateAccessKey(String projectId, String projectTitle) {
        log.info("Update access key has started. projectId: {}, projectTitle: {}", projectId, projectTitle);
        String accessKey = projectFeedAccessKeyMapper.generateAccessKey(projectId, projectTitle);
        projectFeedSettingsRepository.updateAccessKey(projectId, accessKey);
    }

    private Optional<RichTextDto> initializeIfInfoProvided(ProjectFeedSettingsOperationVo projectFeedSettingsOperationVo, ProjectFeedSettings projectFeedSettings) {
        return Optional.of(projectFeedSettingsOperationVo)
                .map(ProjectFeedSettingsOperationVo::getInfo)
                .map(info -> mapInfoRichTextVo(projectFeedSettings.getProjectFeedSettingsId(), info))
                .map(richTextInitializeService::initializeRichText);
    }

    private InitializeRichTextVo mapInfoRichTextVo(String projectFeedId, String info) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(projectFeedId);
        initializeRichTextVo.setValue(info);
        initializeRichTextVo.setType(RichTextType.PROJECT_FEED_INFO);
        return initializeRichTextVo;
    }

    private void mapUpdates(ProjectFeedSettingsOperationVo projectFeedSettingsOperationVo, ProjectFeedSettings projectFeedSettings, Optional<RichTextDto> richTextDtoOptional) {
        Optional.of(projectFeedSettingsOperationVo)
                .map(ProjectFeedSettingsOperationVo::getProjectId)
                .ifPresent(projectFeedSettings::setProjectId);

        Optional.of(projectFeedSettingsOperationVo)
                .map(ProjectFeedSettingsOperationVo::getProjectFeedAccessType)
                .ifPresent(projectFeedSettings::setProjectFeedAccessType);

        Optional.of(projectFeedSettingsOperationVo)
                .map(ProjectFeedSettingsOperationVo::getProjectPostInitializeAccessType)
                .ifPresent(projectFeedSettings::setProjectPostInitializeAccessType);

        Optional.of(projectFeedSettingsOperationVo)
                .map(ProjectFeedSettingsOperationVo::getProjectPostCommentPolicyType)
                .ifPresent(projectFeedSettings::setProjectPostCommentPolicyType);

        Optional.of(projectFeedSettingsOperationVo)
                .map(ProjectFeedSettingsOperationVo::getInfoWebsiteUrl)
                .ifPresent(projectFeedSettings::setInfoWebsiteUrl);

        richTextDtoOptional.map(RichTextDto::getRichTextId)
                .ifPresent(projectFeedSettings::setInfoRichTextId);
    }
}
