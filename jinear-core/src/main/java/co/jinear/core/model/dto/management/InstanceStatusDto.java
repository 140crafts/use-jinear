package co.jinear.core.model.dto.management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InstanceStatusDto {

    private String version;
    @Nullable
    private String latestVersion;
    private Boolean updateAvailable;
    private Boolean updateCheckEnabled;
    private Boolean usageReportEnabled;
    @Nullable
    private ZonedDateTime lastCheckDate;
}
