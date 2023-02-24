package co.jinear.core.model.request.auth;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginWithPasswordRequest extends BaseRequest {
    @NotBlank
    private String email;
    @NotBlank
    @ToString.Exclude
    private String password;
}
