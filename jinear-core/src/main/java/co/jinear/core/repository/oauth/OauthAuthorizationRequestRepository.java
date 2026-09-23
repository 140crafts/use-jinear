package co.jinear.core.repository.oauth;

import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface OauthAuthorizationRequestRepository extends JpaRepository<OauthAuthorizationRequest, String> {

    Optional<OauthAuthorizationRequest> findByOauthAuthorizationRequestIdAndPassiveIdIsNull(String oauthAuthorizationRequestId);

    @Modifying
    @Query("delete from OauthAuthorizationRequest r where r.expiresAt < :before")
    int deleteAllExpiredBefore(@Param("before") Date before);
}
