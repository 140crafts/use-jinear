package co.jinear.core.converter.material;

import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.vo.material.MaterialFileInitializeVo;
import co.jinear.core.model.vo.material.MaterialFolderInitializeVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialEntityConverter {

    Material convert(MaterialFileInitializeVo materialFileInitializeVo);

    Material convert(MaterialFolderInitializeVo materialFolderInitializeVo);

    @AfterMapping
    default void afterMapForFile(@MappingTarget Material material, MaterialFileInitializeVo materialFileInitializeVo) {
        material.setMaterialType(MaterialType.FILE);
    }

    @AfterMapping
    default void afterMapForFolder(@MappingTarget Material material, MaterialFolderInitializeVo materialFolderInitializeVo) {
        material.setMaterialType(MaterialType.FOLDER);
    }
}
