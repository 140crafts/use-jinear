package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTitleUpdateRequest extends BaseRequest {

    @NotNull
    private String title;
}
