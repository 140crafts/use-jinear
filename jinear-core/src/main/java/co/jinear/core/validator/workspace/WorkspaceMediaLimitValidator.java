package co.jinear.core.validator.workspace;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.repository.TaskMediaRepository;
import co.jinear.core.system.NumberCompareHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceMediaLimitValidator {

    private static final Long WORKSPACE_MAX_TOTAL_STORAGE = 102_400L;
    private final TaskMediaRepository taskMediaRepository;

    public void validateWorkspaceStorageLimitNotExceeded(String workspaceId, Long nextFileSize) {
        log.info("Validate workspace storage limit not exceeded has started. workspaceId: {}, nextFileSize: {}", workspaceId, nextFileSize);
        Long currentTotal = taskMediaRepository.sumAllMediaSizeForWorkspace(workspaceId);
        Long nextTotalInMb=(currentTotal + nextFileSize) / 1024 / 1024;
        if (NumberCompareHelper.isGreaterThan(nextTotalInMb, WORKSPACE_MAX_TOTAL_STORAGE)){
            throw new BusinessException("workspace.max-file-size-reached");
        }
    }
}
