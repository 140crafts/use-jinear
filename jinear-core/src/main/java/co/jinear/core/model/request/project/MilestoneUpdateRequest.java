package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MilestoneUpdateRequest extends BaseRequest {

    @NotBlank
    private String milestoneId;
    @Nullable
    private String title;
    @Nullable
    private String description;
    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime targetDate;
    @Nullable
    @Min(0)
    private Integer order;
}
