package co.jinear.core.telemetry;

import co.jinear.core.config.properties.*;
import co.jinear.core.manager.telemetry.InstanceReportSendManager;
import co.jinear.core.model.dto.management.InstanceInfoDto;
import co.jinear.core.model.dto.telemetry.InstanceReportResultDto;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.telemetry.SizeBucket;
import co.jinear.core.model.request.telemetry.InstanceReportRequest;
import co.jinear.core.model.request.telemetry.InstanceUsageReportRequest;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.management.InstanceInfoService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.telemetry.TelemetryApiClient;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstanceReportSendManagerTest {

    private static final String INSTANCE_ID = "0d7c7a4e-3b1f-4c2a-9e55-6f1b2a3c4d5e";

    private TelemetryProperties telemetryProperties;
    private InstanceInfoService instanceInfoService;
    private TelemetryApiClient telemetryApiClient;
    private InstanceReportSendManager manager;

    @BeforeEach
    void setUp() {
        telemetryProperties = new TelemetryProperties();
        telemetryProperties.setVersion("v0.1.1681");
        telemetryProperties.setUpdateCheckEnabled(Boolean.TRUE);
        telemetryProperties.setUsageReportEnabled(Boolean.FALSE);

        FileStorageProperties fileStorageProperties = new FileStorageProperties();
        fileStorageProperties.setActiveFileStorageType(MediaFileProviderType.MINIO);
        MailProperties mailProperties = new MailProperties();
        mailProperties.setMailHost("localhost");

        instanceInfoService = Mockito.mock(InstanceInfoService.class);
        InstanceInfoDto instanceInfoDto = new InstanceInfoDto();
        instanceInfoDto.setTelemetryInstanceId(INSTANCE_ID);
        when(instanceInfoService.retrieve()).thenReturn(instanceInfoDto);

        InstanceFlagService instanceFlagService = Mockito.mock(InstanceFlagService.class);
        Map<InstanceFlagType, Object> flags = new LinkedHashMap<>();
        flags.put(InstanceFlagType.MCP_SERVER, Boolean.TRUE);
        flags.put(InstanceFlagType.FORGOT_PASSWORD, Boolean.FALSE);
        when(instanceFlagService.retrieveAll()).thenReturn(flags);

        AccountRetrieveService accountRetrieveService = Mockito.mock(AccountRetrieveService.class);
        when(accountRetrieveService.approximateActiveAccounts()).thenReturn(SizeBucket.TWO_TO_FIVE);
        WorkspaceRetrieveService workspaceRetrieveService = Mockito.mock(WorkspaceRetrieveService.class);
        when(workspaceRetrieveService.approximateActiveWorkspaces()).thenReturn(SizeBucket.ONE);
        TeamRetrieveService teamRetrieveService = Mockito.mock(TeamRetrieveService.class);
        when(teamRetrieveService.approximateActiveTeams()).thenReturn(SizeBucket.TWENTY_SIX_TO_HUNDRED);
        TaskRetrieveService taskRetrieveService = Mockito.mock(TaskRetrieveService.class);
        when(taskRetrieveService.approximateTasksCreatedAfter(any(Date.class))).thenReturn(SizeBucket.ZERO);

        telemetryApiClient = Mockito.mock(TelemetryApiClient.class);
        InstanceReportResultDto result = new InstanceReportResultDto();
        result.setLatestVersion("v0.1.1690");
        when(telemetryApiClient.sendReport(any(InstanceReportRequest.class))).thenReturn(Optional.of(result));

        manager = new InstanceReportSendManager(telemetryProperties, new McpProperties(), new OauthProperties(),
                new NotificationProperties(), fileStorageProperties, mailProperties, new GenericJinearProperties(),
                instanceInfoService, instanceFlagService, accountRetrieveService, workspaceRetrieveService,
                teamRetrieveService, taskRetrieveService, telemetryApiClient);
    }

    @Test
    void sendsNothingWhenDoNotTrackIsSet() {
        telemetryProperties.setDoNotTrack("1");

        manager.sendReport();

        verify(telemetryApiClient, never()).sendReport(any(InstanceReportRequest.class));
    }

    @Test
    void sendsNothingWhenBothTiersAreOff() {
        telemetryProperties.setUpdateCheckEnabled(Boolean.FALSE);

        manager.sendReport();

        verify(telemetryApiClient, never()).sendReport(any(InstanceReportRequest.class));
    }

    @Test
    void sendsNothingFromABranchBuild() {
        telemetryProperties.setVersion("ab5df1ab");

        manager.sendReport();

        verify(telemetryApiClient, never()).sendReport(any(InstanceReportRequest.class));
    }

    @Test
    void sendsOnlyTheIdAndVersionWhenTheUsageReportIsOff() {
        manager.sendReport();

        InstanceReportRequest sent = captureSent();
        assertThat(sent.getInstanceId()).isEqualTo(INSTANCE_ID);
        assertThat(sent.getVersion()).isEqualTo("v0.1.1681");
        assertThat(sent.getUsage()).isNull();
        verify(instanceInfoService).recordCheck("v0.1.1690");
    }

    @Test
    void sendsBucketedUsageWhenTheUsageReportIsOn() {
        telemetryProperties.setUpdateCheckEnabled(Boolean.FALSE);
        telemetryProperties.setUsageReportEnabled(Boolean.TRUE);

        manager.sendReport();

        InstanceUsageReportRequest usage = captureSent().getUsage();
        assertThat(usage).isNotNull();
        assertThat(usage.getStorageProvider()).isEqualTo(MediaFileProviderType.MINIO);
        assertThat(usage.getEnabledInstanceFlags()).isEqualTo(List.of(InstanceFlagType.MCP_SERVER));
        assertThat(usage.getMailConfigured()).isFalse();
        assertThat(usage.getAccounts()).isEqualTo(SizeBucket.TWO_TO_FIVE);
        assertThat(usage.getWorkspaces()).isEqualTo(SizeBucket.ONE);
        assertThat(usage.getTeams()).isEqualTo(SizeBucket.TWENTY_SIX_TO_HUNDRED);
        assertThat(usage.getTasksCreatedLast30Days()).isEqualTo(SizeBucket.ZERO);
        assertThat(usage.getJavaVersion()).isNotBlank();
    }

    private InstanceReportRequest captureSent() {
        ArgumentCaptor<InstanceReportRequest> captor = ArgumentCaptor.forClass(InstanceReportRequest.class);
        verify(telemetryApiClient).sendReport(captor.capture());
        return captor.getValue();
    }
}
