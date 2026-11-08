package co.jinear.core.model.request.material;

import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class MaterialTextSearchRequest extends BaseRequest {

    @NotBlank
    private String query;
    @Nullable
    private MaterialType materialType;
}
