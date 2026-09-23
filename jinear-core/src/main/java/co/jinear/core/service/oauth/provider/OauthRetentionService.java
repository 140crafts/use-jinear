package co.jinear.core.service.oauth.provider;

import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthRetentionService {

    private static final int SPENT_GRANT_RETENTION_DAYS = 1;
    private static final int UNUSED_CLIENT_RETENTION_DAYS = 7;

    private final OauthAuthorizationCodeService oauthAuthorizationCodeService;
    private final OauthAuthorizationRequestService oauthAuthorizationRequestService;
    private final OauthClientService oauthClientService;

    @Transactional
    public void pruneExpired() {
        Date grantCutoff = DateHelper.substractDays(DateHelper.now(), SPENT_GRANT_RETENTION_DAYS);
        int prunedCodes = oauthAuthorizationCodeService.purgeExpiredBefore(grantCutoff);
        int prunedRequests = oauthAuthorizationRequestService.purgeExpiredBefore(grantCutoff);

        Date clientCutoff = DateHelper.substractDays(DateHelper.now(), UNUSED_CLIENT_RETENTION_DAYS);
        int prunedClients = oauthClientService.purgeUnusedDynamicClientsBefore(clientCutoff);

        if (prunedCodes + prunedRequests + prunedClients > 0) {
            log.info("[OAUTH] Pruned {} authorization codes, {} pending authorization requests and {} unused dynamic clients.",
                    prunedCodes, prunedRequests, prunedClients);
        }
    }
}
