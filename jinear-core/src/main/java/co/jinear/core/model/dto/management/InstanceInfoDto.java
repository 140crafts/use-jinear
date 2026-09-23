package co.jinear.core.model.dto.management;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InstanceInfoDto extends BaseDto {

    private String instanceInfoId;
    private String telemetryInstanceId;
    @Nullable
    private String latestKnownVersion;
    @Nullable
    private ZonedDateTime lastCheckDate;
}
