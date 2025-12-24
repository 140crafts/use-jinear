package co.jinear.core.model.vo.material;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MaterialInitializeVo {

    private String workspaceId;
    private String ownerId;
    private String parentMaterialId;
    private String name;
}
