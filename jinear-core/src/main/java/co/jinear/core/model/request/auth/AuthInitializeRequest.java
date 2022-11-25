package co.jinear.core.model.request.auth;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthInitializeRequest extends BaseRequest {
    private String email;
}
