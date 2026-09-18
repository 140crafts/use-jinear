package co.jinear.core.manager.telemetry;

import co.jinear.core.config.properties.*;
import co.jinear.core.model.dto.telemetry.InstanceReportResultDto;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.request.telemetry.InstanceReportRequest;
import co.jinear.core.model.request.telemetry.InstanceUsageReportRequest;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.management.InstanceInfoService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.telemetry.TelemetryApiClient;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.system.util.DateHelper;
import co.jinear.core.system.util.ReleaseVersionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceReportSendManager {

    private static final int ACTIVITY_WINDOW_DAYS = 30;
    private static final String LOCAL_MAIL_HOST = "localhost";

    private final TelemetryProperties telemetryProperties;
    private final McpProperties mcpProperties;
    private final OauthProperties oauthProperties;
    private final NotificationProperties notificationProperties;
    private final FileStorageProperties fileStorageProperties;
    private final MailProperties mailProperties;
    private final GenericJinearProperties genericJinearProperties;
    private final InstanceInfoService instanceInfoService;
    private final InstanceFlagService instanceFlagService;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final TeamRetrieveService teamRetrieveService;
    private final TaskRetrieveService taskRetrieveService;
    private final TelemetryApiClient telemetryApiClient;

    public void sendReport() {
        boolean updateCheckEnabled = Boolean.TRUE.equals(telemetryProperties.getUpdateCheckEnabled());
        boolean usageReportEnabled = Boolean.TRUE.equals(telemetryProperties.getUsageReportEnabled());
        String version = telemetryProperties.getVersion();
        log.info("Send instance report has started. updateCheckEnabled: {}, usageReportEnabled: {}, doNotTrack: {}, version: {}",
                updateCheckEnabled, usageReportEnabled, telemetryProperties.isDoNotTrackRequested(), version);
        if (!isSendingAllowed(updateCheckEnabled, usageReportEnabled, version)) {
            log.info("Instance report is not sent.");
            return;
        }
        InstanceReportRequest instanceReportRequest = mapRequest(version, usageReportEnabled);
        String latestVersion = telemetryApiClient.sendReport(instanceReportRequest)
                .map(InstanceReportResultDto::getLatestVersion)
                .orElse(null);
        instanceInfoService.recordCheck(latestVersion);
        log.info("Send instance report has completed. latestVersion: {}", latestVersion);
    }

    private boolean isSendingAllowed(boolean updateCheckEnabled, boolean usageReportEnabled, String version) {
        return !telemetryProperties.isDoNotTrackRequested()
                && (updateCheckEnabled || usageReportEnabled)
                && ReleaseVersionHelper.isRelease(version);
    }

    private InstanceReportRequest mapRequest(String version, boolean usageReportEnabled) {
        InstanceReportRequest instanceReportRequest = new InstanceReportRequest();
        instanceReportRequest.setInstanceId(instanceInfoService.retrieve().getTelemetryInstanceId());
        instanceReportRequest.setVersion(version);
        if (usageReportEnabled) {
            instanceReportRequest.setUsage(retrieveUsageReport());
        }
        return instanceReportRequest;
    }

    private InstanceUsageReportRequest retrieveUsageReport() {
        InstanceUsageReportRequest usage = new InstanceUsageReportRequest();
        usage.setStorageProvider(fileStorageProperties.getActiveFileStorageType());
        usage.setEnabledInstanceFlags(retrieveEnabledInstanceFlags());
        usage.setMcpEnabled(Boolean.TRUE.equals(mcpProperties.getEnabled()));
        usage.setOauthEnabled(Boolean.TRUE.equals(oauthProperties.getEnabled()));
        usage.setPushNotificationsEnabled(Boolean.TRUE.equals(notificationProperties.getFirebaseEnabled()));
        usage.setMailConfigured(isMailConfigured());
        usage.setManagementEnabled(Boolean.TRUE.equals(genericJinearProperties.getManagementEnabled()));
        usage.setAccounts(accountRetrieveService.approximateActiveAccounts());
        usage.setWorkspaces(workspaceRetrieveService.approximateActiveWorkspaces());
        usage.setTeams(teamRetrieveService.approximateActiveTeams());
        usage.setTasksCreatedLast30Days(taskRetrieveService.approximateTasksCreatedAfter(DateHelper.substractDays(DateHelper.now(), ACTIVITY_WINDOW_DAYS)));
        usage.setJavaVersion(String.valueOf(Runtime.version().feature()));
        usage.setPostgresVersion(instanceInfoService.retrieveDatabaseMajorVersion());
        usage.setOsArch(System.getProperty("os.arch"));
        return usage;
    }

    private List<InstanceFlagType> retrieveEnabledInstanceFlags() {
        return instanceFlagService.retrieveAll().entrySet().stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    private boolean isMailConfigured() {
        String mailHost = mailProperties.getMailHost();
        return StringUtils.isNotBlank(mailHost) && !LOCAL_MAIL_HOST.equalsIgnoreCase(mailHost);
    }
}
