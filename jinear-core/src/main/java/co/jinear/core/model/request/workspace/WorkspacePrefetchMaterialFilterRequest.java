package co.jinear.core.model.request.workspace;

import co.jinear.core.model.enumtype.material.MaterialSearchContentFilterType;
import co.jinear.core.model.enumtype.material.MaterialSearchSortType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class WorkspacePrefetchMaterialFilterRequest {

    @Nullable
    private MaterialSearchSortType materialSearchSortType;
    @Nullable
    private MaterialSearchContentFilterType materialSearchContentFilterType;
}
