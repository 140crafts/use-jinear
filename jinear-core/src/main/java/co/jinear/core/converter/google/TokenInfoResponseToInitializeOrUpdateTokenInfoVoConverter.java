package co.jinear.core.converter.google;

import co.jinear.core.model.vo.google.InitializeOrUpdateTokenInfoVo;
import co.jinear.core.system.gcloud.auth.model.response.TokenInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenInfoResponseToInitializeOrUpdateTokenInfoVoConverter {

    InitializeOrUpdateTokenInfoVo convert(TokenInfoResponse tokenInfoResponse);
}
