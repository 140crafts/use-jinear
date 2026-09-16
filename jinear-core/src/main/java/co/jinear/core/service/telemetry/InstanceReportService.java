package co.jinear.core.service.telemetry;

import co.jinear.core.model.entity.telemetry.InstanceReport;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.vo.telemetry.InstanceReportVo;
import co.jinear.core.model.vo.telemetry.InstanceUsageReportVo;
import co.jinear.core.repository.telemetry.InstanceReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceReportService {

    private static final String FLAG_SEPARATOR = ",";

    private final InstanceReportRepository instanceReportRepository;

    @Transactional
    public void upsert(InstanceReportVo instanceReportVo) {
        log.info("Upsert instance report has started. instanceId: {}, version: {}, usageShared: {}",
                instanceReportVo.getInstanceId(), instanceReportVo.getVersion(), Objects.nonNull(instanceReportVo.getUsage()));
        InstanceReport instanceReport = instanceReportRepository.findByInstanceIdAndPassiveIdIsNull(instanceReportVo.getInstanceId())
                .orElseGet(() -> initialize(instanceReportVo.getInstanceId()));
        instanceReport.setVersion(instanceReportVo.getVersion());
        instanceReport.setLastReportDate(ZonedDateTime.now());
        applyUsage(instanceReport, instanceReportVo.getUsage());
        instanceReportRepository.save(instanceReport);
    }

    private InstanceReport initialize(String instanceId) {
        InstanceReport instanceReport = new InstanceReport();
        instanceReport.setInstanceId(instanceId);
        return instanceReport;
    }

    private void applyUsage(InstanceReport instanceReport, InstanceUsageReportVo sharedUsage) {
        InstanceUsageReportVo usage = Optional.ofNullable(sharedUsage).orElseGet(InstanceUsageReportVo::new);
        instanceReport.setUsageShared(Objects.nonNull(sharedUsage));
        instanceReport.setStorageProvider(usage.getStorageProvider());
        instanceReport.setEnabledInstanceFlags(joinFlags(usage.getEnabledInstanceFlags()));
        instanceReport.setMcpEnabled(usage.getMcpEnabled());
        instanceReport.setOauthEnabled(usage.getOauthEnabled());
        instanceReport.setPushNotificationsEnabled(usage.getPushNotificationsEnabled());
        instanceReport.setMailConfigured(usage.getMailConfigured());
        instanceReport.setManagementEnabled(usage.getManagementEnabled());
        instanceReport.setAccountsBucket(usage.getAccounts());
        instanceReport.setWorkspacesBucket(usage.getWorkspaces());
        instanceReport.setTeamsBucket(usage.getTeams());
        instanceReport.setTasksCreatedLast30DaysBucket(usage.getTasksCreatedLast30Days());
        instanceReport.setJavaVersion(usage.getJavaVersion());
        instanceReport.setPostgresVersion(usage.getPostgresVersion());
        instanceReport.setOsArch(usage.getOsArch());
    }

    private String joinFlags(List<InstanceFlagType> instanceFlagTypes) {
        return Optional.ofNullable(instanceFlagTypes)
                .map(flags -> flags.stream()
                        .map(InstanceFlagType::name)
                        .sorted()
                        .collect(Collectors.joining(FLAG_SEPARATOR)))
                .orElse(null);
    }
}
