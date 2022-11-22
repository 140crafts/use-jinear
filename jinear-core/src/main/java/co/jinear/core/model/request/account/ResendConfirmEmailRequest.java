package co.jinear.core.model.request.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;

@Getter
@Setter
@ToString(callSuper = true)
public class ResendConfirmEmailRequest extends BaseRequest {
    @Email
    private String email;
    private LocaleType preferredLocaleType = LocaleType.EN;
}
