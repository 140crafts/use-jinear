package co.jinear.core.validator.project;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.service.project.MilestoneRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MilestoneValidator {

    private final MilestoneRetrieveService milestoneRetrieveService;

    public void validateMilestoneIsInProject(String projectId, String milestoneId) {
        log.info("Validate milestone is in project has started. projectId: {}, milestoneId: {}", projectId, milestoneId);
        if (!milestoneRetrieveService.checkExistsByProjectIdAndMilestoneId(projectId, milestoneId)){
            throw new NotFoundException();
        }
    }
}
