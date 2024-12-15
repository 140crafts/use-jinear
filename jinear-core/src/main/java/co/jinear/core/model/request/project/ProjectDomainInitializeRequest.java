package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDomainInitializeRequest extends BaseRequest {

    @NotBlank
    private String projectId;
    @NotBlank
    private String domain;
}
