package co.jinear.core.repository;

import co.jinear.core.model.entity.username.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UsernameRepository extends JpaRepository<Username,String> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Username> findByUsername(String username);

    Long countAllByRelatedObjectIdAndPassiveIdIsNull(String relatedObjectId);
}
