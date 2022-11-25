package co.jinear.core.model.request.account.register;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RegisterViaMailRequest extends BaseRequest {
    @NotBlank
    private String email;
    @NotBlank
    @ToString.Exclude
    private String password;
    @NotNull
    private LocaleType locale;
}
