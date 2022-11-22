package co.jinear.core.model.request.account.register;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RegisterViaMailRequest {
    @NotBlank
    private String email;
    @NotBlank
    @ToString.Exclude
    private String password;
    @NotNull
    private LocaleType preferredLocale;
}
