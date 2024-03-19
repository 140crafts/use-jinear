package co.jinear.core.service.account;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.account.AccountDeleteEligibilityDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.mail.GenericInfoWithSubInfoMailWithCtaButtonVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.service.mail.LocaleStringService;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static co.jinear.core.model.enumtype.token.TokenType.ACCOUNT_DELETION;
import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDeleteService {

    private static final Long DELETION_CONFIRM_TTL = (long) (1000 * 60 * 60 * 24);

    private final TokenService tokenService;
    private final AccountRetrieveService accountRetrieveService;
    private final MailService mailService;
    private final LocaleStringService localeStringService;
    private final FeProperties feProperties;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final WorkspaceMemberListingService workspaceMemberListingService;
    private final AccountUpdateService accountUpdateService;

    public void sendAccountDeleteConfirmationMail(String accountId) {
        log.info("Send account delete confirmation mail has started. accountId: {}", accountId);
        AccountDto accountDto = accountRetrieveService.retrieve(accountId);
        TokenDto tokenDto = tokenService.retrieveValidTokenWithRelatedObject(accountDto.getEmail(), ACCOUNT_DELETION)
                .orElseGet(() -> generateToken(accountDto));
        sendMail(accountDto, tokenDto);
    }

    public void confirmDelete(String token, String currentAccountId) {
        log.info("Confirm delete has started.");
        TokenDto tokenDto = tokenService.retrieveValidToken(token, ACCOUNT_DELETION);
        String accountMail = tokenDto.getRelatedObject();
        AccountDto accountDto = accountRetrieveService.retrieveByEmail(accountMail);
        String accountId = accountDto.getAccountId();
        validateLoggedInUserIsSameWithDeletingUser(currentAccountId, accountDto);
        validateEligibilityAndAnonymize(accountId);
    }

    public void validateEligibilityAndAnonymize(String accountId) {
        log.info("Validate eligibility and anonymize has started. accountId: {}", accountId);
        validateEligibility(accountId);
        accountUpdateService.anonymizeAccount(accountId);
    }

    private static void validateLoggedInUserIsSameWithDeletingUser(String currentAccountId, AccountDto accountDto) {
        if (!currentAccountId.equalsIgnoreCase(accountDto.getAccountId())) {
            throw new BusinessException();
        }
    }

    public AccountDeleteEligibilityDto retrieveEligibility(String accountId) {
        List<DetailedWorkspaceMemberDto> workspaces = workspaceRetrieveService.retrieveAccountWorkspaces(accountId);
        List<DetailedWorkspaceMemberDto> ownedWorkspacesWithActiveMembers = workspaces.stream()
                .filter(detailedWorkspaceMemberDto -> OWNER.equals(detailedWorkspaceMemberDto.getRole()))
                .filter(detailedWorkspaceMemberDto -> workspaceMemberListingService.workspaceActiveMemberCount(detailedWorkspaceMemberDto.getWorkspaceId()) > 1L)
                .toList();

        AccountDeleteEligibilityDto accountDeleteEligibilityDto = new AccountDeleteEligibilityDto();
        accountDeleteEligibilityDto.setEligible(ownedWorkspacesWithActiveMembers.isEmpty());
        accountDeleteEligibilityDto.setWorkspacesWithActiveMembers(ownedWorkspacesWithActiveMembers);
        return accountDeleteEligibilityDto;
    }

    public void validateEligibility(String accountId) {
        AccountDeleteEligibilityDto accountDeleteEligibilityDto = retrieveEligibility(accountId);
        if (!accountDeleteEligibilityDto.isEligible()) {
            throw new BusinessException();
        }
    }

    private void sendMail(AccountDto accountDto, TokenDto tokenDto) {
        String email = accountDto.getEmail();
        LocaleType preferredLocale = Optional.of(accountDto).map(AccountDto::getLocaleType).orElse(LocaleType.EN);
        String title = localeStringService.retrieveLocalString(LocaleStringType.ACCOUNT_DELETION_MAIL_TITLE, preferredLocale);
        String text = localeStringService.retrieveLocalString(LocaleStringType.ACCOUNT_DELETION_MAIL_TEXT, preferredLocale);
        String subtext = localeStringService.retrieveLocalString(LocaleStringType.ACCOUNT_DELETION_MAIL_SUBTEXT, preferredLocale);
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.ACCOUNT_DELETION_MAIL_CTA_LABEL, preferredLocale);
        String href = feProperties.getAccountDeleteUrl().replaceAll(Pattern.quote("{token}"), tokenDto.getUniqueToken());

        GenericInfoWithSubInfoMailWithCtaButtonVo genericInfoWithSubInfoMailWithCtaButtonVo = new GenericInfoWithSubInfoMailWithCtaButtonVo(email, preferredLocale, title, title, text, subtext, href, ctaLabel);

        try {
            mailService.sendGenericInfoWithSubInfoWithCtaButtonMail(genericInfoWithSubInfoMailWithCtaButtonVo);
        } catch (Exception e) {
            log.error("Send account delete mail has failed.", e);
            throw new BusinessException();
        }
    }

    private TokenDto generateToken(AccountDto accountDto) {
        log.info("Generate token has started.");
        String token = NormalizeHelper.normalizeStrictly(UUID.randomUUID().toString());
        GenerateTokenVo vo = GenerateTokenVo.builder()
                .relatedObject(accountDto.getEmail())
                .tokenType(ACCOUNT_DELETION)
                .uniqueToken(token)
                .commonToken(token)
                .ttl(DELETION_CONFIRM_TTL)
                .build();
        return tokenService.generateToken(vo);
    }
}
