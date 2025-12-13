package co.jinear.core.model.request.material;

import co.jinear.core.model.enumtype.material.MaterialSearchContentFilterType;
import co.jinear.core.model.enumtype.material.MaterialSearchSortType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class MaterialSearchRequest extends BaseRequest {

    @Min(0)
    private int page = 0;
    @NotNull
    private String workspaceId;
    @Nullable
    private String parentMaterialId;
    @Nullable
    private MaterialSearchSortType materialSearchSortType = MaterialSearchSortType.IDATE_DESC;
    @Nullable
    private MaterialSearchContentFilterType materialSearchContentFilterType;
    @Nullable
    private Boolean shared;
    @Nullable
    private Boolean deleted;
}
