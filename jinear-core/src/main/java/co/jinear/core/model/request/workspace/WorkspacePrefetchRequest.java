package co.jinear.core.model.request.workspace;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
@ToString
public class WorkspacePrefetchRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @Valid
    @NotNull
    @Size(max = 12)
    private List<WorkspacePrefetchTimespanRequest> calendarTimespans;
    @Valid
    @NotNull
    @Size(max = 8)
    private List<WorkspacePrefetchMaterialFilterRequest> materialFilters;
    @Nullable
    @Min(1)
    private Integer teamLimit;
    private boolean includeTeamTaskMedia;
}
