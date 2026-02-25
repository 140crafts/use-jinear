package co.jinear.core.model.request.auth;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class SingleUseTokenLoginRequest extends BaseRequest {

    @NotBlank
    private String uniqueToken;
    @NotBlank
    private String commonToken;
    @Nullable
    private String timeZone;
}
