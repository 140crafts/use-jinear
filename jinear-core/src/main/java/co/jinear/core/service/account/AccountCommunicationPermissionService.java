package co.jinear.core.service.account;

import co.jinear.core.converter.account.AccountCommunicationPermissionDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.entity.account.AccountCommunicationPermission;
import co.jinear.core.model.vo.account.UpdateAccountCommunicationPermissionsVo;
import co.jinear.core.repository.AccountCommunicationPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCommunicationPermissionService {

    private final AccountCommunicationPermissionRepository accountCommunicationPermissionRepository;
    private final AccountCommunicationPermissionDtoConverter accountCommunicationPermissionDtoConverter;

    public AccountCommunicationPermissionDto initialize(String accountId) {
        log.info("Initialize account communication permission has started. accountId: {}", accountId);
        validateAccountDontHaveAnyPermissionInitialized(accountId);
        AccountCommunicationPermission saved = createInitialPermission(accountId);
        return accountCommunicationPermissionDtoConverter.convert(saved);
    }

    public AccountCommunicationPermissionDto retrieve(String accountId) {
        log.info("Retrieve account communication permission has started. accountId: {}", accountId);
        return accountCommunicationPermissionRepository.findByAccountIdAndPassiveIdIsNull(accountId)
                .map(accountCommunicationPermissionDtoConverter::convert)
                .orElseGet(() -> initialize(accountId));
    }

    public AccountCommunicationPermissionDto update(UpdateAccountCommunicationPermissionsVo updateAccountCommunicationPermissionsVo) {
        log.info("Update account communication permission has started. updateAccountCommunicationPermissionsVo: {}", updateAccountCommunicationPermissionsVo);
        AccountCommunicationPermission communicationPermission = retrieveEntity(updateAccountCommunicationPermissionsVo.getAccountId());
        communicationPermission.setEmail(updateAccountCommunicationPermissionsVo.getEmail());
        communicationPermission.setPushNotification(updateAccountCommunicationPermissionsVo.getPushNotification());
        AccountCommunicationPermission saved = accountCommunicationPermissionRepository.save(communicationPermission);
        log.info("Update account communication permission has completed.");
        return accountCommunicationPermissionDtoConverter.convert(saved);
    }

    public AccountCommunicationPermissionDto updatePushNotification(String accountId, Boolean pushNotification) {
        log.info("Update account push notification communication permission has started. accountId: {}, pushNotification: {}", accountId, pushNotification);
        AccountCommunicationPermission communicationPermission = retrieveEntity(accountId);
        communicationPermission.setPushNotification(pushNotification);
        AccountCommunicationPermission saved = accountCommunicationPermissionRepository.save(communicationPermission);
        log.info("Update account  push notification communication permission has completed.");
        return accountCommunicationPermissionDtoConverter.convert(saved);
    }

    private AccountCommunicationPermission retrieveEntity(String accountId) {
        log.info("Retrieve account communication permission has started. accountId: {}", accountId);
        return accountCommunicationPermissionRepository.findByAccountIdAndPassiveIdIsNull(accountId)
                .orElseGet(() -> createInitialPermission(accountId));
    }

    private void validateAccountDontHaveAnyPermissionInitialized(String accountId) {
        accountCommunicationPermissionRepository.findByAccountIdAndPassiveIdIsNull(accountId).ifPresent(p -> {
            throw new BusinessException();
        });
    }

    private AccountCommunicationPermission createInitialPermission(String accountId) {
        AccountCommunicationPermission communicationPermission = new AccountCommunicationPermission();
        communicationPermission.setAccountId(accountId);
        communicationPermission.setEmail(Boolean.TRUE);
        communicationPermission.setPushNotification(Boolean.FALSE);
        return accountCommunicationPermissionRepository.save(communicationPermission);
    }
}
