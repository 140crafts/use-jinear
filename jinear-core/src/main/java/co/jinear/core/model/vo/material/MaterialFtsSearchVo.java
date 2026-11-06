package co.jinear.core.model.vo.material;

import co.jinear.core.model.enumtype.material.MaterialType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MaterialFtsSearchVo {

    private String query;
    private String workspaceId;
    private MaterialType materialType;
    private String accountId;
    private boolean workspaceAdmin;
    private int page;
}
