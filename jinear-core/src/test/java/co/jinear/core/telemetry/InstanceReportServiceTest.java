package co.jinear.core.telemetry;

import co.jinear.core.model.entity.telemetry.InstanceReport;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.telemetry.SizeBucket;
import co.jinear.core.model.vo.telemetry.InstanceReportVo;
import co.jinear.core.model.vo.telemetry.InstanceUsageReportVo;
import co.jinear.core.repository.telemetry.InstanceReportRepository;
import co.jinear.core.service.telemetry.InstanceReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstanceReportServiceTest {

    private static final String INSTANCE_ID = "0d7c7a4e-3b1f-4c2a-9e55-6f1b2a3c4d5e";

    private InstanceReportRepository repository;
    private InstanceReportService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(InstanceReportRepository.class);
        service = new InstanceReportService(repository);
    }

    @Test
    void clearsUsageColumnsWhenTheInstanceStopsSharingUsage() {
        InstanceReport existing = new InstanceReport();
        existing.setInstanceId(INSTANCE_ID);
        existing.setUsageShared(Boolean.TRUE);
        existing.setMcpEnabled(Boolean.TRUE);
        existing.setAccountsBucket(SizeBucket.ONE);
        existing.setEnabledInstanceFlags("MCP_SERVER");
        when(repository.findByInstanceIdAndPassiveIdIsNull(INSTANCE_ID)).thenReturn(Optional.of(existing));

        service.upsert(report(null));

        InstanceReport saved = captureSaved();
        assertThat(saved.getVersion()).isEqualTo("v0.1.2");
        assertThat(saved.getLastReportDate()).isNotNull();
        assertThat(saved.getUsageShared()).isFalse();
        assertThat(saved.getMcpEnabled()).isNull();
        assertThat(saved.getAccountsBucket()).isNull();
        assertThat(saved.getEnabledInstanceFlags()).isNull();
    }

    @Test
    void storesSharedUsageOnAFirstReport() {
        when(repository.findByInstanceIdAndPassiveIdIsNull(INSTANCE_ID)).thenReturn(Optional.empty());
        InstanceUsageReportVo usage = new InstanceUsageReportVo();
        usage.setStorageProvider(MediaFileProviderType.MINIO);
        usage.setEnabledInstanceFlags(List.of(InstanceFlagType.MCP_SERVER, InstanceFlagType.ATTACH_GOOGLE_CALENDAR));
        usage.setMcpEnabled(Boolean.TRUE);
        usage.setAccounts(SizeBucket.SIX_TO_TWENTY_FIVE);

        service.upsert(report(usage));

        InstanceReport saved = captureSaved();
        assertThat(saved.getInstanceId()).isEqualTo(INSTANCE_ID);
        assertThat(saved.getUsageShared()).isTrue();
        assertThat(saved.getStorageProvider()).isEqualTo(MediaFileProviderType.MINIO);
        assertThat(saved.getEnabledInstanceFlags()).isEqualTo("ATTACH_GOOGLE_CALENDAR,MCP_SERVER");
        assertThat(saved.getAccountsBucket()).isEqualTo(SizeBucket.SIX_TO_TWENTY_FIVE);
    }

    private InstanceReportVo report(InstanceUsageReportVo usage) {
        InstanceReportVo instanceReportVo = new InstanceReportVo();
        instanceReportVo.setInstanceId(INSTANCE_ID);
        instanceReportVo.setVersion("v0.1.2");
        instanceReportVo.setUsage(usage);
        return instanceReportVo;
    }

    private InstanceReport captureSaved() {
        ArgumentCaptor<InstanceReport> captor = ArgumentCaptor.forClass(InstanceReport.class);
        verify(repository).save(captor.capture());
        return captor.getValue();
    }
}
