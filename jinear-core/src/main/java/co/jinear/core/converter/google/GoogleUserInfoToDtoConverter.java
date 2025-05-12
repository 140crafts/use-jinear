package co.jinear.core.converter.google;

import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.entity.google.GoogleUserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoogleUserInfoToDtoConverter {

    GoogleUserInfoDto convert(GoogleUserInfo googleUserInfo);
}
