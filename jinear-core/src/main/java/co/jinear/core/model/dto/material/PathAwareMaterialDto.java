package co.jinear.core.model.dto.material;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathAwareMaterialDto extends MaterialDto {

    private MaterialPathDto materialPath;
}
