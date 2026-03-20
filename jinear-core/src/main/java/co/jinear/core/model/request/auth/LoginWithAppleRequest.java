package co.jinear.core.model.request.auth;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class LoginWithAppleRequest extends BaseRequest {

    @Nullable
    private Boolean webClient = Boolean.FALSE;
    @NotBlank
    private String code;
    @Nullable
    private String timeZone;
}
