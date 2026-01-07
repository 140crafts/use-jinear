package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMediaUsageDto {

    private Long storageLimit;
    private Long currentTotal;
    private WorkspaceTier nextTier;
    private Long nextTierStorageLimit;
}
