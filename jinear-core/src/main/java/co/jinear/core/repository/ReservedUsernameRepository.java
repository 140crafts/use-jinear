package co.jinear.core.repository;

import co.jinear.core.model.entity.username.ReservedUsername;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedUsernameRepository extends JpaRepository<ReservedUsername, Integer> {

    Long countAllByUsername(String username);
}
