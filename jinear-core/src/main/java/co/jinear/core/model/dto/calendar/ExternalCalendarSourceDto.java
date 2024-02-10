package co.jinear.core.model.dto.calendar;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class ExternalCalendarSourceDto {

    private String externalCalendarSourceId;
    @Nullable
    private String summary;
    @Nullable
    private String description;
    @Nullable
    private String location;
    @Nullable
    private String timeZone;
}
