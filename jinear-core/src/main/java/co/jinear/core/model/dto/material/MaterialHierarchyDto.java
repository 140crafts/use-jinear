package co.jinear.core.model.dto.material;

import co.jinear.core.model.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialHierarchyDto {

    private PathAwareMaterialDto container;
    private PageDto<MaterialDto> content;
}
