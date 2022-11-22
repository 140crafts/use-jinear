package co.jinear.core.service.auth;

import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.vo.auth.AuthResponseVo;
import co.jinear.core.model.vo.auth.AuthVo;

public interface AuthenticationStrategy {

    AuthResponseVo auth(AuthVo authVo);

    ProviderType getType();
}
