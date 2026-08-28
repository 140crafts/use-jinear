package co.jinear.core.repository.management;

import co.jinear.core.model.entity.management.InstanceFlag;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstanceFlagRepository extends JpaRepository<InstanceFlag, String> {

    List<InstanceFlag> findAllByPassiveIdIsNullOrderByInstanceFlagId();

    Optional<InstanceFlag> findFirstByFlagType(InstanceFlagType instanceFlagType);
}
