package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
public class TaskSearchRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @Nullable
    private List<String> teamIdList;
    @NotBlank
    private String query;
}
