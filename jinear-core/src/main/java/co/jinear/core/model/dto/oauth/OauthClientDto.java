package co.jinear.core.model.dto.oauth;

import co.jinear.core.model.enumtype.oauth.OauthClientRegistrationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class OauthClientDto {

    private String clientId;
    private String clientName;
    private String clientUri;
    private String logoUri;
    private List<String> redirectUris;
    private OauthClientRegistrationType registrationType;
    private Date clientIdIssuedAt;
}
