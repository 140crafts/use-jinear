package co.jinear.core.model.request.workspace;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
@ToString
public class WorkspaceActivityFilterRequest extends BaseRequest {

    @Nullable
    private Integer page = 0;
    @NotBlank
    private String workspaceId;
    @Nullable
    private List<String> teamIdList;
    @Nullable
    private List<String> taskIds;
}
