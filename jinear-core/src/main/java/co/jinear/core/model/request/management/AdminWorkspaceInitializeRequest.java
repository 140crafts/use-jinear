package co.jinear.core.model.request.management;

import co.jinear.core.model.request.workspace.WorkspaceInitializeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdminWorkspaceInitializeRequest extends WorkspaceInitializeRequest {

    @NotBlank
    private String ownerAccountId;
}
