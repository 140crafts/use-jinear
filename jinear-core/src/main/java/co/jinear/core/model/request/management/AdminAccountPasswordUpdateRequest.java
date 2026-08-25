package co.jinear.core.model.request.management;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdminAccountPasswordUpdateRequest extends BaseRequest {

    @NotNull
    @Size(min = 6, max = 512)
    @ToString.Exclude
    private String newPassword;
}
