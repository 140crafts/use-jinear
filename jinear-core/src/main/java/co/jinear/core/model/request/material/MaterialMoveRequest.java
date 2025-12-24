package co.jinear.core.model.request.material;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class MaterialMoveRequest extends BaseRequest {

    @NotBlank
    private String materialId;
    @Nullable
    private String parentMaterialId;
}
