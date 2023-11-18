package co.jinear.core.repository;

import co.jinear.core.model.entity.servicelog.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceLogRepository extends JpaRepository<ServiceLog, Long> {
}
