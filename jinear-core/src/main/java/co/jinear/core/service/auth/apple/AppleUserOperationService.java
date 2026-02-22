package co.jinear.core.service.auth.apple;

import co.jinear.core.converter.account.IdTokenPayloadToAppleUserConverter;
import co.jinear.core.model.entity.account.AppleUser;
import co.jinear.core.repository.AppleUserRepository;
import co.jinear.core.service.client.apple.model.IdTokenPayload;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleUserOperationService {

    private final AppleUserRepository appleUserRepository;
    private final IdTokenPayloadToAppleUserConverter idTokenPayloadToAppleUserConverter;

    public void initialize(IdTokenPayload idTokenPayload, String accountId) {
        AppleUser appleUser = idTokenPayloadToAppleUserConverter.map(idTokenPayload, accountId);
        appleUserRepository.save(appleUser);
    }

    public Optional<String> retrieveAccountIdWithAppleUserId(String appleUserId) {
        log.info("Retrieve account id with apple user id has started. appleUserId: {}", appleUserId);
        return appleUserRepository.findByExternalAppleIdAndPassiveIdIsNull(appleUserId).map(AppleUser::getAccountId);
    }

    public void deleteWithAccountId(String accountId, String passiveId) {
        log.info("Delete with account id has started. accountId: {}, passiveId: {}", accountId, passiveId);
        appleUserRepository.deleteAppleUser(accountId, passiveId, DateHelper.now());
    }
}
