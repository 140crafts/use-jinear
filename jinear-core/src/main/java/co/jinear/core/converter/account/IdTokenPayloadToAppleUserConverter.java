package co.jinear.core.converter.account;

import co.jinear.core.model.entity.account.AppleUser;
import co.jinear.core.service.client.apple.model.IdTokenPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IdTokenPayloadToAppleUserConverter {

    @Mapping(target = "externalAppleId", source = "idTokenPayload.sub")
    @Mapping(target = "appleMail", source = "idTokenPayload.email")
    @Mapping(target = "appleName", source = "idTokenPayload.aud")
//todo check
    AppleUser map(IdTokenPayload idTokenPayload, String accountId);
}
