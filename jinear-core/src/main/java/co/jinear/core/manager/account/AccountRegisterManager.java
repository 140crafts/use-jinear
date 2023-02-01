package co.jinear.core.manager.account;

import co.jinear.core.converter.account.AccountInitializeVoConverter;
import co.jinear.core.model.request.account.register.RegisterViaMailRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.service.account.AccountInitializeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountRegisterManager {

    private final AccountInitializeService accountInitializeService;
    private final AccountInitializeVoConverter accountInitializeVoConverter;

    public BaseResponse registerViaMail(RegisterViaMailRequest registerViaMailRequest) {
        log.info("Register via mail has started. email: {}", registerViaMailRequest.getEmail());
        AccountInitializeVo accountInitializeVo = accountInitializeVoConverter.map(registerViaMailRequest);
        accountInitializeVo.setEmailConfirmed(Boolean.FALSE);
        accountInitializeService.initializeAccount(accountInitializeVo);
        return new BaseResponse();
    }
}
