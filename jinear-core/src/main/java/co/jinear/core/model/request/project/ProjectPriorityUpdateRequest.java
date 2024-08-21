package co.jinear.core.model.request.project;

import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectPriorityUpdateRequest extends BaseRequest {

    @NotNull
    private ProjectPriorityType projectPriority;
}
