package co.jinear.core.model.vo.material;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MaterialFolderInitializeVo extends MaterialInitializeVo {

    private String icon;
    private String color;
}
