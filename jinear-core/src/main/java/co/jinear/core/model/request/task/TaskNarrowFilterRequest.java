package co.jinear.core.model.request.task;

import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

import static co.jinear.core.model.enumtype.FilterSort.IDATE_DESC;

@Getter
@Setter
@ToString
public class TaskNarrowFilterRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @Nullable
    private List<String> teamIdList;
    @NotNull
    private ZonedDateTime timespanStart;
    @NotNull
    private ZonedDateTime timespanEnd;
    @Nullable
    private FilterSort sort = IDATE_DESC;
    @Nullable
    private List<TaskExternalCalendarFilterRequest> externalCalendarList;
}
