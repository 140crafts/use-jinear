package co.jinear.core.repository.oauth;

import co.jinear.core.model.entity.oauth.OauthAuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface OauthAuthorizationCodeRepository extends JpaRepository<OauthAuthorizationCode, String> {

    Optional<OauthAuthorizationCode> findByOauthAuthorizationCodeIdAndPassiveIdIsNull(String oauthAuthorizationCodeId);

    @Modifying
    @Query("delete from OauthAuthorizationCode c where c.expiresAt < :before")
    int deleteAllExpiredBefore(@Param("before") Date before);
}
