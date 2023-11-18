package co.jinear.core.converter.google;

import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.entity.google.GoogleToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoogleTokenToDtoConverter {

    GoogleTokenDto convert(GoogleToken googleToken);
}
