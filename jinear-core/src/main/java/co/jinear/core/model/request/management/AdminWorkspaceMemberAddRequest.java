package co.jinear.core.model.request.management;

import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdminWorkspaceMemberAddRequest extends BaseRequest {

    @NotBlank
    private String accountId;
    @NotNull
    private WorkspaceAccountRoleType role;
}
