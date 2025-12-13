package co.jinear.core.model.dto.material;

import co.jinear.core.model.enumtype.material.MaterialType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MaterialPathItemDto {

    private String materialId;
    private String parentMaterialId;
    private String name;
    private MaterialType materialType;
}
