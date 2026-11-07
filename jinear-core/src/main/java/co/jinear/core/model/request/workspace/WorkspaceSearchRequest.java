package co.jinear.core.model.request.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceSearchType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
public class WorkspaceSearchRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @NotBlank
    private String query;
    @Nullable
    private List<String> teamIdList;
    @Nullable
    private List<@NotNull WorkspaceSearchType> types;
}
