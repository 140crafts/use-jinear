package co.jinear.core.repository;

import co.jinear.core.model.entity.SessionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {
}
