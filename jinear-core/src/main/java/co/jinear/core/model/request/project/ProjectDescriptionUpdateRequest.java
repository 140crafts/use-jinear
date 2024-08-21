package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class ProjectDescriptionUpdateRequest extends BaseRequest {

    @Nullable
    private String description;
}
