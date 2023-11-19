package co.jinear.core.model.response.auth;

import co.jinear.core.model.response.BaseResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRedirectInfoResponse extends BaseResponse {

    private String redirectUrl;
}
