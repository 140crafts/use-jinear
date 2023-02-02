package co.jinear.core.converter.account;

import co.jinear.core.model.request.account.InitializeResetPasswordRequest;
import co.jinear.core.model.vo.account.password.InitializeResetPasswordVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeResetPasswordVoConverter {

    InitializeResetPasswordVo map(InitializeResetPasswordRequest initializeResetPasswordRequest);
}
