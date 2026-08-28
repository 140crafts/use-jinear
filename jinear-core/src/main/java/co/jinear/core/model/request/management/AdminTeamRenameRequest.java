package co.jinear.core.model.request.management;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdminTeamRenameRequest extends BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String name;
}
