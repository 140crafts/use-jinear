package co.jinear.core.model.request.oauth;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveMobileLoginRedirectInfoRequest extends BaseRequest {

    @NotNull
    private String csrf;
}
