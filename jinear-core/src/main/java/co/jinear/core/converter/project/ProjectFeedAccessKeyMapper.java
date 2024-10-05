package co.jinear.core.converter.project;

import co.jinear.core.system.NormalizeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectFeedAccessKeyMapper {

    private static final int MAX_TITLE_SAMPLE_LENGTH = 101;//128 - projectId.length - '-'.length

    public String generateAccessKey(String projectId, String projectTitle) {
        log.info("Generate access key for project feed has started. projectId: {}, projectTitle: {}", projectId, projectTitle);
        String normalizedProjectTitle = NormalizeHelper.normalizeUsernameReplaceSpaces(projectTitle);
        normalizedProjectTitle = normalizedProjectTitle.substring(0, Math.min(normalizedProjectTitle.length(), MAX_TITLE_SAMPLE_LENGTH));
        String accessKey = "%s-%s".formatted(normalizedProjectTitle, projectId);
        log.info("Access key generated. accessKey: {}", accessKey);
        return accessKey;
    }

    public static void main(String[] args) {
        String str = "First Project Title çağdaş É";
        System.out.println(NormalizeHelper.normalizeUsernameReplaceSpaces(str));
    }
}
