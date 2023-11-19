package co.jinear.core.converter.google;

import co.jinear.core.model.enumtype.google.GoogleScopeType;
import co.jinear.core.model.vo.google.InitializeGoogleTokenScopeVo;
import co.jinear.core.system.NormalizeHelper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GoogleScopeConverter {

    public List<InitializeGoogleTokenScopeVo> convertToInitializeGoogleTokenScopeVo(String googleTokenId, String scopes) {
        return convert(scopes)
                .stream()
                .map(googleScopeType -> new InitializeGoogleTokenScopeVo(googleTokenId, googleScopeType))
                .toList();
    }

    public List<GoogleScopeType> convert(String scopes) {
        return Arrays.stream(scopes.split(NormalizeHelper.SPACE_STRING))
                .map(GoogleScopeType::fromString)
                .toList();
    }
}
