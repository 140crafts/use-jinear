package co.jinear.core.repository.management;

import co.jinear.core.model.entity.management.InstanceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InstanceInfoRepository extends JpaRepository<InstanceInfo, String> {

    Optional<InstanceInfo> findFirstByPassiveIdIsNullOrderByCreatedDateAsc();

    @Query(value = "select current_setting('server_version_num')", nativeQuery = true)
    String retrieveServerVersionNumber();
}
