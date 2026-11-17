package co.jinear.core.model.request.account;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ConfirmEmailRequest extends BaseRequest {
    @NotBlank
    @Size(max = 255)
    private String email;
    @NotBlank
    @ToString.Exclude
    @Size(max = 16)
    private String code;
}
