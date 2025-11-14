package co.jinear.core.model.request.auth;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class LoginWithPasswordRequest extends BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @ToString.Exclude
    @Size(max = 255)
    private String password;
    @Nullable
    private String timeZone;
}
