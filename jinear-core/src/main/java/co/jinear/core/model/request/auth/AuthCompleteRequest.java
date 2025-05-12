package co.jinear.core.model.request.auth;

import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthCompleteRequest extends BaseRequest {
    private String email;
    private ProviderType provider;
    private String csrf;
    private String code;
    private String timeZone;
}
