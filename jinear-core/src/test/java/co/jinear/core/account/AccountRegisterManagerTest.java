package co.jinear.core.account;

import co.jinear.core.config.properties.MailProperties;
import co.jinear.core.converter.account.AccountInitializeVoConverter;
import co.jinear.core.manager.account.AccountRegisterManager;
import co.jinear.core.model.request.account.register.RegisterViaMailRequest;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.service.account.AccountInitializeService;
import co.jinear.core.service.management.InstanceFlagService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountRegisterManagerTest {

    private final AccountInitializeService accountInitializeService = Mockito.mock(AccountInitializeService.class);
    private final AccountInitializeVoConverter accountInitializeVoConverter = Mockito.mock(AccountInitializeVoConverter.class);
    private final MailProperties mailProperties = new MailProperties();
    private final AccountRegisterManager accountRegisterManager = new AccountRegisterManager(
            accountInitializeService,
            accountInitializeVoConverter,
            Mockito.mock(InstanceFlagService.class),
            mailProperties);

    @Test
    void accountWaitsForConfirmationWhenMailIsConfigured() {
        mailProperties.setMailHost("smtp.real-host.com");

        assertEquals(Boolean.FALSE, registerAndCaptureEmailConfirmed());
    }

    @Test
    void accountIsConfirmedWhenMailHostIsTheInstallerPlaceholder() {
        mailProperties.setMailHost("smtp.example.com");

        assertEquals(Boolean.TRUE, registerAndCaptureEmailConfirmed());
    }

    @Test
    void accountIsConfirmedWhenMailHostIsBlank() {
        mailProperties.setMailHost("");

        assertEquals(Boolean.TRUE, registerAndCaptureEmailConfirmed());
    }

    private Boolean registerAndCaptureEmailConfirmed() {
        RegisterViaMailRequest registerViaMailRequest = new RegisterViaMailRequest();
        Mockito.when(accountInitializeVoConverter.map(registerViaMailRequest)).thenReturn(new AccountInitializeVo());

        accountRegisterManager.registerViaMail(registerViaMailRequest);

        ArgumentCaptor<AccountInitializeVo> captor = ArgumentCaptor.forClass(AccountInitializeVo.class);
        Mockito.verify(accountInitializeService).initializeAccount(captor.capture());
        return captor.getValue().getEmailConfirmed();
    }
}
