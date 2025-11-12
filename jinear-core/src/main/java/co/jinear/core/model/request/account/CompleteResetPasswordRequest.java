package co.jinear.core.model.request.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CompleteResetPasswordRequest extends BaseRequest {
    @NotBlank
    @Size(max = 255)
    private String uniqueToken;
    private LocaleType locale = LocaleType.EN;
}
