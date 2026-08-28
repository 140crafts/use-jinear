package co.jinear.core.model.request.management;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdminAccountCreateRequest extends BaseRequest {

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
    @NotBlank
    @Size(min = 6, max = 512)
    @ToString.Exclude
    private String password;
}
