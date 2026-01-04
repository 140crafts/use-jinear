package co.jinear.core.model.dto.material;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.enumtype.material.MaterialType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

@Getter
@Setter
@ToString
public class MaterialDto extends BaseDto {

    private String materialId;
    private String workspaceId;
    private String ownerId;
    private String parentMaterialId;
    private String mediaId;
    private MaterialType materialType;
    private String name;
    private String icon;
    private String color;
    private AccessibleMediaDto media;
    private MaterialAccessType materialAccessType;
    private Set<MaterialAccessDto> materialAccesses;
    @Nullable
    private PlainAccountProfileDto owner;
}
