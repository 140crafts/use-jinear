package co.jinear.core.model.request.account;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ConfirmEmailRequest extends BaseRequest {
    private String uniqueToken;
}
