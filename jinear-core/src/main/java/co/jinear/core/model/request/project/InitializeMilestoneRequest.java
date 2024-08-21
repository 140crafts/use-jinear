package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InitializeMilestoneRequest extends BaseRequest {

    @NotBlank
    private String projectId;
    @NotBlank
    private String title;
    @Nullable
    private String description;
    @Nullable
    private ZonedDateTime targetDate;
}
