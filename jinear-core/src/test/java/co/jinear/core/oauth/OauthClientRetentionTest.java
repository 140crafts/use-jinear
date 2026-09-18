package co.jinear.core.oauth;

import co.jinear.core.model.entity.oauth.OauthClient;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.enumtype.oauth.OauthClientRegistrationType;
import co.jinear.core.repository.oauth.OauthClientRepository;
import co.jinear.core.repository.oauth.OauthConnectionRepository;
import co.jinear.core.system.util.DateHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "co.jinear.core.model.entity.oauth")
@EnableJpaRepositories(basePackages = "co.jinear.core.repository.oauth")
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.liquibase.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:oauth-retention;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
class OauthClientRetentionTest {

    private static final Date CUTOFF = DateHelper.substractDays(DateHelper.now(), 7);

    @Autowired
    private OauthClientRepository oauthClientRepository;

    @Autowired
    private OauthConnectionRepository oauthConnectionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private OauthClient client(String clientId, OauthClientRegistrationType type, int ageInDays) {
        OauthClient client = new OauthClient();
        client.setClientId(clientId);
        client.setClientName(clientId);
        client.setRegistrationType(type);
        client.setClientIdIssuedAt(DateHelper.substractDays(DateHelper.now(), ageInDays));
        return oauthClientRepository.save(client);
    }

    private void connectionFor(String clientId, boolean revoked) {
        OauthConnection connection = new OauthConnection();
        connection.setAccountId("account-1");
        connection.setClientId(clientId);
        connection.setClientName(clientId);
        connection.setGrantedScopes("tasks:read");
        if (revoked) {
            connection.setPassiveId("passive-1");
        }
        oauthConnectionRepository.save(connection);
    }

    private int purge() {
        int deleted = oauthClientRepository.deleteUnusedRegisteredBefore(OauthClientRegistrationType.DCR, CUTOFF);
        entityManager.clear();
        return deleted;
    }

    @Test
    void deletesADynamicClientThatNeverConnectedAndIsOldEnough() {
        client("spam-1", OauthClientRegistrationType.DCR, 30);

        assertThat(purge()).isEqualTo(1);
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("spam-1")).isEmpty();
    }

    @Test
    void keepsARecentDynamicClientSoAnAbandonedConsentCanBeRetried() {
        client("fresh-1", OauthClientRegistrationType.DCR, 1);

        assertThat(purge()).isZero();
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("fresh-1")).isPresent();
    }

    @Test
    void keepsADynamicClientThatEverConnected() {
        client("used-1", OauthClientRegistrationType.DCR, 30);
        connectionFor("used-1", false);

        assertThat(purge()).isZero();
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("used-1")).isPresent();
    }

    @Test
    void keepsADynamicClientWhoseOnlyConnectionWasRevoked() {
        client("disconnected-1", OauthClientRegistrationType.DCR, 30);
        connectionFor("disconnected-1", true);

        assertThat(purge()).isZero();
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("disconnected-1")).isPresent();
    }

    @Test
    void neverTouchesCimdOrStaticClients() {
        client("https://claude.ai/client.json", OauthClientRegistrationType.CIMD, 30);
        client("static-1", OauthClientRegistrationType.STATIC, 30);

        assertThat(purge()).isZero();
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("https://claude.ai/client.json")).isPresent();
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("static-1")).isPresent();
    }

    @Test
    void deletesOnlyTheUnusedOnesWhenBothKindsArePresent() {
        client("spam-1", OauthClientRegistrationType.DCR, 30);
        client("spam-2", OauthClientRegistrationType.DCR, 30);
        client("used-1", OauthClientRegistrationType.DCR, 30);
        connectionFor("used-1", false);

        assertThat(purge()).isEqualTo(2);
        assertThat(oauthClientRepository.findByClientIdAndPassiveIdIsNull("used-1")).isPresent();
    }
}
