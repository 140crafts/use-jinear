package co.jinear.core.repository;

import co.jinear.core.model.entity.passive.Passive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassiveRepository extends JpaRepository<Passive, String> {
}
