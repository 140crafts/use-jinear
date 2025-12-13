package co.jinear.core.model.dto.material;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MaterialPathDto {
    private String materialId;
    private List<MaterialPathItemDto> path;
    private String fullPath;
}
