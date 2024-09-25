package co.jinear.core.converter.project;

import co.jinear.core.system.NormalizeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectFeedAccessKeyMapper {

    public String generateAccessKey(String projectId, String projectTitle) {
        log.info("Generate access key for project feed has started. projectId: {}, projectTitle: {}", projectId, projectTitle);
        String normalizedProjectTitle = NormalizeHelper.normalizeStrictly(projectTitle);
        String accessKey = "%s-%s".formatted(normalizedProjectTitle, projectId);
        log.info("Access key generated. accessKey: {}", accessKey);
        return accessKey;
    }
}
