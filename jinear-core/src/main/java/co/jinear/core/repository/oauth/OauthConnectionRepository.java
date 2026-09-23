package co.jinear.core.repository.oauth;

import co.jinear.core.model.entity.oauth.OauthConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OauthConnectionRepository extends JpaRepository<OauthConnection, String> {

    Optional<OauthConnection> findByOauthConnectionIdAndPassiveIdIsNull(String oauthConnectionId);

    List<OauthConnection> findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(String accountId);

    Optional<OauthConnection> findFirstByAccountIdAndClientIdAndPassiveIdIsNull(String accountId, String clientId);

    long countByPassiveIdIsNull();
}
