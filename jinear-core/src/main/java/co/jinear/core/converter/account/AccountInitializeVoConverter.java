package co.jinear.core.converter.account;

import co.jinear.core.model.request.account.register.RegisterViaMailRequest;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountInitializeVoConverter {

    AccountInitializeVo map(RegisterViaMailRequest registerViaMailRequest);
}
