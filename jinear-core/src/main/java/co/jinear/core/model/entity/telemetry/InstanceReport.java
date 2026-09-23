package co.jinear.core.model.entity.telemetry;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.telemetry.SizeBucket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "instance_report")
public class InstanceReport extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "instance_report_id")
    private String instanceReportId;

    @Column(name = "instance_id", nullable = false)
    private String instanceId;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "last_report_date", nullable = false)
    private ZonedDateTime lastReportDate;

    @Column(name = "usage_shared", nullable = false)
    private Boolean usageShared;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_provider")
    private MediaFileProviderType storageProvider;

    @Column(name = "enabled_instance_flags", columnDefinition = "text")
    private String enabledInstanceFlags;

    @Column(name = "mcp_enabled")
    private Boolean mcpEnabled;

    @Column(name = "oauth_enabled")
    private Boolean oauthEnabled;

    @Column(name = "push_notifications_enabled")
    private Boolean pushNotificationsEnabled;

    @Column(name = "mail_configured")
    private Boolean mailConfigured;

    @Column(name = "management_enabled")
    private Boolean managementEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "accounts_bucket")
    private SizeBucket accountsBucket;

    @Enumerated(EnumType.STRING)
    @Column(name = "workspaces_bucket")
    private SizeBucket workspacesBucket;

    @Enumerated(EnumType.STRING)
    @Column(name = "teams_bucket")
    private SizeBucket teamsBucket;

    @Enumerated(EnumType.STRING)
    @Column(name = "tasks_created_last_30_days_bucket")
    private SizeBucket tasksCreatedLast30DaysBucket;

    @Column(name = "java_version")
    private String javaVersion;

    @Column(name = "postgres_version")
    private String postgresVersion;

    @Column(name = "os_arch")
    private String osArch;
}
