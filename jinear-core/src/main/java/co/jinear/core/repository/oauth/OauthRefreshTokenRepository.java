package co.jinear.core.repository.oauth;

import co.jinear.core.model.entity.oauth.OauthRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OauthRefreshTokenRepository extends JpaRepository<OauthRefreshToken, String> {

    Optional<OauthRefreshToken> findByOauthRefreshTokenIdAndPassiveIdIsNull(String oauthRefreshTokenId);

    List<OauthRefreshToken> findAllByOauthConnectionIdAndPassiveIdIsNull(String oauthConnectionId);
}
