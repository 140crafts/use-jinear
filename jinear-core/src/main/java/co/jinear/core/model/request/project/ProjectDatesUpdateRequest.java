package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ProjectDatesUpdateRequest extends BaseRequest {
    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startDate;
    @Nullable
    private Boolean updateStartDate;
    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime targetDate;
    @Nullable
    private Boolean updateTargetDate;
}
