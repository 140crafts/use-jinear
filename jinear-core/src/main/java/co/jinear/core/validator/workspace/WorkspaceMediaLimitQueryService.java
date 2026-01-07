package co.jinear.core.validator.workspace;

import co.jinear.core.config.properties.GenericJinearProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMediaUsageDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.repository.TaskMediaRepository;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.system.NumberCompareHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceMediaLimitQueryService {

    private final GenericJinearProperties genericJinearProperties;
    private final TaskMediaRepository taskMediaRepository;
    private final WorkspaceRetrieveService workspaceRetrieveService;

    public WorkspaceMediaUsageDto retrieveWorkspaceMediaUsage(String workspaceId) {
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        WorkspaceTier currentTier = workspaceDto.getTier();
        Long storageLimit = getLimit(currentTier);
        Long currentTotal = taskMediaRepository.sumAllMediaSizeForWorkspace(workspaceId);

        WorkspaceMediaUsageDto workspaceMediaUsageDto = new WorkspaceMediaUsageDto();
        workspaceMediaUsageDto.setStorageLimit(storageLimit);
        workspaceMediaUsageDto.setCurrentTotal(currentTotal / 1024 / 1024);
        if (WorkspaceTier.BASIC.equals(currentTier)) {
            workspaceMediaUsageDto.setNextTier(WorkspaceTier.PRO);
            workspaceMediaUsageDto.setNextTierStorageLimit(getLimit(WorkspaceTier.PRO));
        }
        return workspaceMediaUsageDto;
    }

    public void validateWorkspaceStorageLimitNotExceeded(String workspaceId, Long nextFileSize) {
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceStorageLimitNotExceeded(workspaceId, nextFileSize, workspaceDto.getTier());
    }

    public void validateWorkspaceStorageLimitNotExceeded(String workspaceId, Long nextFileSize, WorkspaceTier workspaceTier) {
        Long storageLimit = getLimit(workspaceTier);
        log.info("Validate workspace storage limit not exceeded has started. workspaceId: {}, nextFileSize: {}", workspaceId, nextFileSize);
        Long currentTotal = taskMediaRepository.sumAllMediaSizeForWorkspace(workspaceId);
        currentTotal = Objects.isNull(currentTotal) ? 0L : currentTotal;
        Long nextTotalInMb = (currentTotal + nextFileSize) / 1024 / 1024;
        if (NumberCompareHelper.isGreaterThan(nextTotalInMb, storageLimit)) {
            throw new BusinessException("workspace.max-file-size-reached");
        }
    }

    private Long getLimit(WorkspaceTier workspaceTier) {
        if (WorkspaceTier.PRO.equals(workspaceTier)) {
            return genericJinearProperties.getStorageLimitPro();
        }
        return genericJinearProperties.getStorageLimitBasic();
    }
}
