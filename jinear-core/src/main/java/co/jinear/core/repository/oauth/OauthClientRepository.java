package co.jinear.core.repository.oauth;

import co.jinear.core.model.entity.oauth.OauthClient;
import co.jinear.core.model.enumtype.oauth.OauthClientRegistrationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface OauthClientRepository extends JpaRepository<OauthClient, String> {

    Optional<OauthClient> findByClientIdAndPassiveIdIsNull(String clientId);

    Page<OauthClient> findAllByPassiveIdIsNullOrderByCreatedDateDesc(Pageable pageable);

    @Modifying
    @Query("""
            delete from OauthClient c
            where c.registrationType = :registrationType
            and c.clientIdIssuedAt < :before
            and not exists (select oc.oauthConnectionId from OauthConnection oc where oc.clientId = c.clientId)
            """)
    int deleteUnusedRegisteredBefore(@Param("registrationType") OauthClientRegistrationType registrationType,
                                     @Param("before") Date before);
}
