package co.jinear.core.model.request.auth;

import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthCompleteRequest extends BaseRequest {
    @Size(max = 255)
    private String email;
    @NotNull
    private ProviderType provider;
    @Size(max = 64)
    private String csrf;
    @Size(max = 64)
    private String code;
    @Size(max = 5)
    private String timeZone;
}
