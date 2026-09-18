package co.jinear.core.model.request.telemetry;

import co.jinear.core.system.util.ReleaseVersionHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

// Not a BaseRequest: this body is the whole payload documented in docs/telemetry.md
@Getter
@Setter
@ToString
public class InstanceReportRequest {

    private static final String INSTANCE_ID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @NotBlank
    @Pattern(regexp = INSTANCE_ID_PATTERN)
    private String instanceId;

    @NotBlank
    @Pattern(regexp = ReleaseVersionHelper.RELEASE_VERSION_PATTERN)
    private String version;

    @Valid
    @Nullable
    private InstanceUsageReportRequest usage;
}
