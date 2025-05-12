package co.jinear.core.model.dto.google;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.google.GoogleScopeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleTokenScopeDto extends BaseDto {

    private String googleTokenScopeId;
    private String googleTokenId;
    private GoogleScopeType scope;
}
