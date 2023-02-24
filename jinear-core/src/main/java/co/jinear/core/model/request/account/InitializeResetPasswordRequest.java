package co.jinear.core.model.request.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeResetPasswordRequest extends BaseRequest {
    @Email
    private String email;
    @NotNull
    private LocaleType locale;
}
