package co.jinear.core.model.request.project;

import co.jinear.core.model.enumtype.project.ProjectStateType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectStateUpdateRequest extends BaseRequest {

    @NotNull
    private ProjectStateType projectState;
}
