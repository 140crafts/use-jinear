package co.jinear.core.converter.google;

import co.jinear.core.model.vo.google.InitializeOrUpdateGoogleAuthTokenVo;
import co.jinear.core.system.gcloud.auth.model.response.AuthTokenResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthTokenResponseToGoogleAuthTokenVoConverter {

    InitializeOrUpdateGoogleAuthTokenVo convert(AuthTokenResponse authTokenResponse, String googleUserInfoId);
}
