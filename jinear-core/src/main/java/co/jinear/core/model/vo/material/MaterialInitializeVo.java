package co.jinear.core.model.vo.material;

import co.jinear.core.model.enumtype.material.MaterialAccessType;
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
    private MaterialAccessType materialAccessType;
}
