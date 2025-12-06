package co.jinear.core.model.vo.material;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MaterialFileInitializeVo extends MaterialInitializeVo {

    private String contentType;
    private long fileSize;
}
