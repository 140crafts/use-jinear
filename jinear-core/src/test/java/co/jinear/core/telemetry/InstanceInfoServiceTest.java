package co.jinear.core.telemetry;

import co.jinear.core.converter.management.InstanceInfoDtoConverter;
import co.jinear.core.model.entity.management.InstanceInfo;
import co.jinear.core.repository.management.InstanceInfoRepository;
import co.jinear.core.service.management.InstanceInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstanceInfoServiceTest {

    private InstanceInfoRepository repository;
    private InstanceInfoService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(InstanceInfoRepository.class);
        service = new InstanceInfoService(repository, Mockito.mock(InstanceInfoDtoConverter.class));
        when(repository.save(any(InstanceInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void receiverKeepsTheLastReleaseWhileRunningABranchBuild() {
        givenStoredLatestVersion("v0.1.9999");

        assertThat(service.recordReleaseVersion("ab5df1ab")).isEqualTo("v0.1.9999");
        verify(repository, never()).save(any(InstanceInfo.class));
    }

    @Test
    void receiverReturnsNullWhenItHasNeverRunARelease() {
        when(repository.findFirstByPassiveIdIsNullOrderByCreatedDateAsc()).thenReturn(Optional.empty());

        assertThat(service.recordReleaseVersion("ab5df1ab")).isNull();
    }

    @Test
    void receiverKeepsTheHighestReleaseAfterARollback() {
        givenStoredLatestVersion("v0.1.9999");

        assertThat(service.recordReleaseVersion("v0.1.9998")).isEqualTo("v0.1.9999");
    }

    @Test
    void receiverStoresANewerRelease() {
        InstanceInfo instanceInfo = givenStoredLatestVersion("v0.1.9999");

        assertThat(service.recordReleaseVersion("v0.1.10000")).isEqualTo("v0.1.10000");
        verify(repository).save(instanceInfo);
    }

    @Test
    void senderIgnoresNullBranchAndOlderLatestVersions() {
        InstanceInfo instanceInfo = givenStoredLatestVersion("v0.1.9999");

        service.recordCheck(null);
        service.recordCheck("ab5df1ab");
        service.recordCheck("v0.1.9000");

        assertThat(instanceInfo.getLatestKnownVersion()).isEqualTo("v0.1.9999");
        assertThat(instanceInfo.getLastCheckDate()).isNotNull();
    }

    @Test
    void senderStoresANewerLatestVersion() {
        InstanceInfo instanceInfo = givenStoredLatestVersion("v0.1.9999");

        service.recordCheck("v0.1.10001");

        assertThat(instanceInfo.getLatestKnownVersion()).isEqualTo("v0.1.10001");
    }

    private InstanceInfo givenStoredLatestVersion(String latestKnownVersion) {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setTelemetryInstanceId("0d7c7a4e-3b1f-4c2a-9e55-6f1b2a3c4d5e");
        instanceInfo.setLatestKnownVersion(latestKnownVersion);
        when(repository.findFirstByPassiveIdIsNullOrderByCreatedDateAsc()).thenReturn(Optional.of(instanceInfo));
        return instanceInfo;
    }
}
