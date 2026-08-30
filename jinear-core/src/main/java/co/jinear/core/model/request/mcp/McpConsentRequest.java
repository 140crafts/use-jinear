package co.jinear.core.model.request.mcp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McpConsentRequest {

    @NotBlank
    private String requestId;

    @NotNull
    private Boolean approved;
}
