package co.jinear.core.service.account;

import co.jinear.core.converter.account.AccountDtoConverter;
import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.repository.AccountRepository;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.workspace.WorkspaceDisplayPreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountRetrieveService {

    private static final int ALL_ACCOUNTS_PAGE_SIZE = 50;

    private final AccountRepository accountRepository;
    private final MediaRetrieveService mediaRetrieveService;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final AccountDtoConverter accountDtoConverter;
    private final PlainAccountProfileDtoConverter plainAccountProfileDtoConverter;

    public AccountDto retrieve(String accountId) {
        log.info("Retrieving account with accountId: {}", accountId);
        return Optional.ofNullable(accountId)
                .map(accountRepository::findByAccountIdAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(accountDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<AccountDto> retrieveOptional(String accountId) {
        log.info("Retrieving optional account with accountId: {}", accountId);
        return Optional.ofNullable(accountId)
                .map(accountRepository::findByAccountIdAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(accountDtoConverter::map);
    }

    public Optional<AccountDto> retrieveByEmailOptional(String email) {
        log.info("Retrieving account with email: {}", email);
        return Optional.ofNullable(email)
                .map(accountRepository::findByEmailAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(accountDtoConverter::map);
    }

    public AccountDto retrieveByEmail(String email) {
        log.info("Retrieving account with email: {}", email);
        return Optional.ofNullable(email)
                .map(accountRepository::findByEmailAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(accountDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public AccountDto retrieveWithBasicInfo(String accountId) {
        log.info("Retrieve account with basic info has started. accountId: {}", accountId);
        AccountDto accountDto = retrieve(accountId);
        setProfilePicture(accountId, accountDto);
        setPreferredWorkspace(accountId, accountDto);
        return accountDto;
    }

    public PlainAccountProfileDto retrievePlainAccountProfile(String accountId) {
        log.info("Retrieve account with plain profile has started. accountId: {}", accountId);
        Optional<AccountDto> accountDtoOptional = retrieveOptional(accountId);
        if (accountDtoOptional.isPresent()) {
            AccountDto accountDto = accountDtoOptional.get();
            setProfilePicture(accountId, accountDto);
            return plainAccountProfileDtoConverter.map(accountDto);
        }
        return null;
    }

    public Boolean exist(String accountId) {
        return accountRepository.countAllByAccountIdAndPassiveIdIsNull(accountId) > 0L;
    }

    public Page<AccountDto> retrieveAllAccounts(int page) {
        log.info("Retrieve all accounts has started. page: {}", page);
        return accountRepository.findAllByGhostFalseAndPassiveIdIsNullOrderByCreatedDateDesc(PageRequest.of(page, ALL_ACCOUNTS_PAGE_SIZE))
                .map(accountDtoConverter::map);
    }

    private void setProfilePicture(String accountId, AccountDto accountDto) {
        mediaRetrieveService.retrieveProfilePictureOptional(accountId)
                .ifPresent(accountDto::setProfilePicture);
    }

    private void setPreferredWorkspace(String accountId, AccountDto accountDto) {
        workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspaceOptional(accountId)
                .ifPresent(accountDto::setWorkspaceDisplayPreference);
    }
}
