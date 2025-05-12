package co.jinear.core.converter.media;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.service.media.fileoperation.MediaFileOperationServiceFactory;
import co.jinear.core.service.media.fileoperation.MediaFileOperationStrategy;
import co.jinear.core.system.FileStorageUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccessibleMediaDtoConverter {

    @Autowired
    protected MediaFileOperationServiceFactory mediaFileOperationServiceFactory;

    public abstract AccessibleMediaDto mapToAccessibleMediaDto(Media media);

    @AfterMapping
    void afterMap(@MappingTarget AccessibleMediaDto accessibleMediaDto, Media media) {
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
        String fullPath = mediaFileOperationStrategy.getFullPath(media.getBucketName(), media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey(), media.getOriginalName());
        String storagePath = FileStorageUtils.generatePath(media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey(), media.getOriginalName());

        accessibleMediaDto.setStoragePath(storagePath);
        accessibleMediaDto.setUrl(fullPath);
    }
}
