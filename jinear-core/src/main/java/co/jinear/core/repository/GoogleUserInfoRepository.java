package co.jinear.core.repository;

import co.jinear.core.model.entity.google.GoogleUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleUserInfoRepository extends JpaRepository<GoogleUserInfo, String> {

    Optional<GoogleUserInfo> findBySubAndEmailAndPassiveIdIsNull(String sub, String email);

}
