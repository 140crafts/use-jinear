package co.jinear.core.repository.telemetry;

import co.jinear.core.model.entity.telemetry.InstanceReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstanceReportRepository extends JpaRepository<InstanceReport, String> {

    Optional<InstanceReport> findByInstanceIdAndPassiveIdIsNull(String instanceId);
}
