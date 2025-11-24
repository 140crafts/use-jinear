package co.jinear.core.model.request.account.register;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterViaMailRequest extends BaseRequest {
    @NotBlank
    @Size(max = 255)
    private String email;
    @NotBlank
    @ToString.Exclude
    @Size(max = 255)
    private String password;
    private LocaleType locale;
}
