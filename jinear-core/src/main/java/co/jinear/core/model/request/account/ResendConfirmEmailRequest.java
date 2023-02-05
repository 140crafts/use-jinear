package co.jinear.core.model.request.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ResendConfirmEmailRequest extends BaseRequest {
    private String token;
    private LocaleType locale = LocaleType.EN;
}
