package co.jinear.core.model.dto.telemetry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class InstanceReportResultDto {

    @Nullable
    private String latestVersion;
}
