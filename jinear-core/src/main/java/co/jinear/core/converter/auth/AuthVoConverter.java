package co.jinear.core.converter.auth;

import co.jinear.core.model.request.auth.AuthCompleteRequest;
import co.jinear.core.model.request.auth.AuthInitializeRequest;
import co.jinear.core.model.response.auth.AuthInitializeResponse;
import co.jinear.core.model.vo.auth.AuthVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthVoConverter {

    AuthVo map(AuthInitializeRequest authInitializeRequest);

    AuthVo map(AuthCompleteRequest authCompleteRequest);

    AuthInitializeResponse map(AuthVo authVo);
}
