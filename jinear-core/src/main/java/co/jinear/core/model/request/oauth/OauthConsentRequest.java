package co.jinear.core.model.request.oauth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OauthConsentRequest {

    @NotBlank
    private String requestId;

    @NotNull
    private Boolean approved;
}
