package co.jinear.core.converter.oauth;

import co.jinear.core.model.request.oauth.OauthAuthorizeRequest;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OauthAuthorizeRequestVoConverter {

    OauthAuthorizeRequestVo map(OauthAuthorizeRequest oauthAuthorizeRequest);
}
