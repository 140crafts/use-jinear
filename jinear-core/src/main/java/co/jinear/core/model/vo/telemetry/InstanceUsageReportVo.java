package co.jinear.core.model.vo.telemetry;

import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.telemetry.SizeBucket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class InstanceUsageReportVo {

    private MediaFileProviderType storageProvider;
    private List<InstanceFlagType> enabledInstanceFlags;
    private Boolean mcpEnabled;
    private Boolean oauthEnabled;
    private Boolean pushNotificationsEnabled;
    private Boolean mailConfigured;
    private Boolean managementEnabled;
    private SizeBucket accounts;
    private SizeBucket workspaces;
    private SizeBucket teams;
    private SizeBucket tasksCreatedLast30Days;
    private String javaVersion;
    private String postgresVersion;
    private String osArch;
}
