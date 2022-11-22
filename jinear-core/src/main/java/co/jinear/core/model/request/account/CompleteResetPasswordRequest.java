package co.jinear.core.model.request.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
public class CompleteResetPasswordRequest extends BaseRequest {
    @NotBlank
    private String uniqueToken;
    private LocaleType localeType = LocaleType.EN;
}
