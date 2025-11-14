package co.jinear.core.model.request.account;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ConfirmEmailRequest extends BaseRequest {
    @Size(max = 64)
    private String uniqueToken;
}
