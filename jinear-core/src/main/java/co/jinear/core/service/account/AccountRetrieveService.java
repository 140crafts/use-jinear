package co.jinear.core.service.account;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.repository.AccountRepository;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.workspace.WorkspaceDisplayPreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountRetrieveService {

    private final AccountRepository accountRepository;
    private final MediaRetrieveService mediaRetrieveService;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final ModelMapper modelMapper;

    public AccountDto retrieve(String accountId) {
        log.info("Retrieving account with accountId: {}", accountId);
        return Optional.ofNullable(accountId)
                .map(accountRepository::findByAccountIdAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(account -> modelMapper.map(account, AccountDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public Optional<AccountDto> retrieveOptional(String accountId) {
        log.info("Retrieving optional account with accountId: {}", accountId);
        return Optional.ofNullable(accountId)
                .map(accountRepository::findByAccountIdAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(account -> modelMapper.map(account, AccountDto.class));
    }

    public Optional<AccountDto> retrieveByEmail(String email) {
        log.info("Retrieving account with email: {}", email);
        return Optional.ofNullable(email)
                .map(accountRepository::findByEmailAndPassiveIdIsNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(account -> modelMapper.map(account, AccountDto.class));
    }

    public AccountDto retrieveWithBasicInfo(String accountId) {
        log.info("Retrieve account with basic info has started. accountId: {}", accountId);
        AccountDto accountDto = retrieve(accountId);
        setProfilePicture(accountId, accountDto);
        setPreferredWorkspaceId(accountId, accountDto);
        return accountDto;
    }

    public Boolean exist(String accountId) {
        return accountRepository.countAllByAccountIdAndPassiveIdIsNull(accountId) > 0L;
    }

    private void setProfilePicture(String accountId, AccountDto accountDto) {
        mediaRetrieveService.retrieveProfilePictureOptional(accountId)
                .ifPresent(accountDto::setProfilePicture);
    }

    private void setPreferredWorkspaceId(String accountId, AccountDto accountDto) {
        workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspace(accountId)
                .ifPresent(workspaceDisplayPreferenceDto -> {
                    accountDto.setPreferredWorkspaceId(workspaceDisplayPreferenceDto.getPreferredWorkspaceId());
                    accountDto.setPreferredTeamId(workspaceDisplayPreferenceDto.getPreferredTeamId());
                });
    }
}
