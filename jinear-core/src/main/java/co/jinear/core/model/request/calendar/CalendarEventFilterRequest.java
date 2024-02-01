package co.jinear.core.model.request.calendar;

import co.jinear.core.model.dto.calendar.TaskExternalCalendarFilterDto;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class CalendarEventFilterRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @Nullable
    private List<String> teamIdList;
    @Nullable
    private List<TaskExternalCalendarFilterDto> externalCalendarList;
    @NotNull
    private ZonedDateTime timespanStart;
    @NotNull
    private ZonedDateTime timespanEnd;

}
