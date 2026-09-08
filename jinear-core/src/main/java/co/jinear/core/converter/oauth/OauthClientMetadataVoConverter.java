package co.jinear.core.converter.oauth;

import co.jinear.core.model.request.oauth.OauthClientRegistrationRequest;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OauthClientMetadataVoConverter {

    OauthClientMetadataVo map(OauthClientRegistrationRequest oauthClientRegistrationRequest);
}
