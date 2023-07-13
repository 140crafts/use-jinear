package co.jinear.core.converter.media;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.system.FileStorageUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MediaDtoConverter {

    AccessibleMediaDto mapToAccessibleMediaDto(Media media);

    MediaDto map(Media media);

    @AfterMapping
    default void afterMap(@MappingTarget AccessibleMediaDto accessibleMediaDto, Media media) {
        String storagePath = FileStorageUtils.generatePath(media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey(), media.getOriginalName());
        accessibleMediaDto.setStoragePath(storagePath);
    }
}
