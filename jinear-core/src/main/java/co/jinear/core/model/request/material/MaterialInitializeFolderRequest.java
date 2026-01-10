package co.jinear.core.model.request.material;

import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import static co.jinear.core.model.enumtype.material.MaterialAccessType.OWNER_ONLY;

@Getter
@Setter
public class MaterialInitializeFolderRequest extends BaseRequest {

    @NotBlank
    private String name;
    @Nullable
    private String icon;
    @Nullable
    private String color;
    @Nullable
    private String parentMaterialId;
    @Nullable
    private MaterialAccessType materialAccessType = OWNER_ONLY;
}
