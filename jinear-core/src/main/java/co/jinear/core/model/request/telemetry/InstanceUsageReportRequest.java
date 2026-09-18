package co.jinear.core.model.request.telemetry;

import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.telemetry.SizeBucket;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
@ToString
public class InstanceUsageReportRequest {

    @NotNull
    private MediaFileProviderType storageProvider;

    @NotNull
    @Size(max = 32)
    private List<InstanceFlagType> enabledInstanceFlags;

    @NotNull
    private Boolean mcpEnabled;

    @NotNull
    private Boolean oauthEnabled;

    @NotNull
    private Boolean pushNotificationsEnabled;

    @NotNull
    private Boolean mailConfigured;

    @NotNull
    private Boolean managementEnabled;

    @NotNull
    private SizeBucket accounts;

    @NotNull
    private SizeBucket workspaces;

    @NotNull
    private SizeBucket teams;

    @NotNull
    private SizeBucket tasksCreatedLast30Days;

    @Nullable
    @Size(max = 32)
    private String javaVersion;

    @Nullable
    @Size(max = 32)
    private String postgresVersion;

    @Nullable
    @Size(max = 32)
    private String osArch;
}
