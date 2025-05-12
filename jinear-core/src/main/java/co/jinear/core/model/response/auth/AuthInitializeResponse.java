package co.jinear.core.model.response.auth;

import co.jinear.core.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthInitializeResponse extends BaseResponse {
    private String email;
    private String csrf;
    private Integer preferredLocaleId;
    private String code;
}
