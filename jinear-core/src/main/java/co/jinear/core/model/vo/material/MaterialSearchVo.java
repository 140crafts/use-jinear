package co.jinear.core.model.vo.material;

import co.jinear.core.model.enumtype.material.MaterialSearchContentFilterType;
import co.jinear.core.model.enumtype.material.MaterialSearchSortType;
import co.jinear.core.model.enumtype.material.MaterialType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MaterialSearchVo {

    private int page = 0;
    private String workspaceId;
    private String parentMaterialId;
    private MaterialSearchSortType materialSearchSortType = MaterialSearchSortType.IDATE_DESC;
    private MaterialType materialType;
    private MaterialSearchContentFilterType materialSearchContentFilterType;
}
