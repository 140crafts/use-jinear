package co.jinear.core.repository;

import co.jinear.core.model.entity.passive.Passive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassiveRepository extends JpaRepository<Passive, String> {

    Optional<Passive> findByPassiveId(String passiveId);
}
