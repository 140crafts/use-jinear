package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectTeamOperationRequest extends BaseRequest {

    @NotBlank
    private String projectId;
    @NotNull
    private List<String> teamIds;
}
