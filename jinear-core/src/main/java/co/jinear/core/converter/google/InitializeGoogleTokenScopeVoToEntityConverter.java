package co.jinear.core.converter.google;

import co.jinear.core.model.entity.google.GoogleTokenScope;
import co.jinear.core.model.vo.google.InitializeGoogleTokenScopeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeGoogleTokenScopeVoToEntityConverter {

    GoogleTokenScope convert(InitializeGoogleTokenScopeVo initializeGoogleTokenScopeVo);
}
