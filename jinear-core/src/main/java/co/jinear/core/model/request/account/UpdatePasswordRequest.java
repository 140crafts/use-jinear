package co.jinear.core.model.request.account;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest extends BaseRequest {
    private String currentPassword;
    private String newPassword;
}
