package co.jinear.core.model.request.material;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialRenameRequest extends BaseRequest {

    @NotBlank
    private String materialId;
    @NotBlank
    private String newName;
}
