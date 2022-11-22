package co.jinear.core.model.response.auth;

import co.jinear.core.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthResponse extends BaseResponse {

    private String token;
}
