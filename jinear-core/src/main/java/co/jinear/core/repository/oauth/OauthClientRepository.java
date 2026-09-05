package co.jinear.core.repository.oauth;

import co.jinear.core.model.entity.oauth.OauthClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthClientRepository extends JpaRepository<OauthClient, String> {

    Optional<OauthClient> findByClientIdAndPassiveIdIsNull(String clientId);

    Page<OauthClient> findAllByPassiveIdIsNullOrderByCreatedDateDesc(Pageable pageable);
}
