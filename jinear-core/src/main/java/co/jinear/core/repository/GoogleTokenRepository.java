package co.jinear.core.repository;

import co.jinear.core.model.entity.google.GoogleToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleTokenRepository extends JpaRepository<GoogleToken, String> {

    Optional<GoogleToken> findByGoogleUserInfoIdAndPassiveIdIsNull(String googleUserInfoId);
}
