package co.jinear.core.converter.google;

import co.jinear.core.model.dto.google.GoogleTokenScopeDto;
import co.jinear.core.model.entity.google.GoogleTokenScope;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoogleTokenScopeDtoConverter {

    GoogleTokenScopeDto convert(GoogleTokenScope googleTokenScope);
}
