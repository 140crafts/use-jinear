package co.jinear.core.account;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.service.account.AccountEmailOwnershipService;
import co.jinear.core.service.account.AccountPasswordService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.account.AccountUpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class AccountEmailOwnershipServiceTest {

    private static final String ACCOUNT_ID = "account-1";

    private final AccountRetrieveService accountRetrieveService = Mockito.mock(AccountRetrieveService.class);
    private final AccountPasswordService accountPasswordService = Mockito.mock(AccountPasswordService.class);
    private final AccountUpdateService accountUpdateService = Mockito.mock(AccountUpdateService.class);
    private final AccountEmailOwnershipService service = new AccountEmailOwnershipService(
            accountRetrieveService,
            accountPasswordService,
            accountUpdateService);

    @Test
    void unconfirmedAccountLosesItsPasswordAndIsConfirmed() {
        givenAccount(Boolean.FALSE);

        service.confirmEmailOwnership(ACCOUNT_ID);

        InOrder inOrder = Mockito.inOrder(accountPasswordService, accountUpdateService);
        inOrder.verify(accountPasswordService).replaceWithRandomPassword(ACCOUNT_ID);
        inOrder.verify(accountUpdateService).updateEmailConfirmed(ACCOUNT_ID, Boolean.TRUE);
    }

    @Test
    void nullEmailConfirmedIsTreatedAsUnconfirmed() {
        givenAccount(null);

        service.confirmEmailOwnership(ACCOUNT_ID);

        Mockito.verify(accountPasswordService).replaceWithRandomPassword(ACCOUNT_ID);
        Mockito.verify(accountUpdateService).updateEmailConfirmed(ACCOUNT_ID, Boolean.TRUE);
    }

    @Test
    void confirmedAccountKeepsItsPassword() {
        givenAccount(Boolean.TRUE);

        service.confirmEmailOwnership(ACCOUNT_ID);

        Mockito.verifyNoInteractions(accountPasswordService, accountUpdateService);
    }

    private void givenAccount(Boolean emailConfirmed) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(ACCOUNT_ID);
        accountDto.setEmailConfirmed(emailConfirmed);
        Mockito.when(accountRetrieveService.retrieve(ACCOUNT_ID)).thenReturn(accountDto);
    }
}
