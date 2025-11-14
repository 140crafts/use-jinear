package co.jinear.core.model.request.workspace;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMemberInvitationRespondRequest extends BaseRequest {

    @NotBlank
    @Size(max = 64)
    private String token;

    @NotNull
    private Boolean accepted;
}
