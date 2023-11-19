package co.jinear.core.model.vo.google;

import co.jinear.core.model.enumtype.google.GoogleScopeType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InitializeGoogleTokenScopeVo {

    private String googleTokenId;
    private GoogleScopeType scope;

}
