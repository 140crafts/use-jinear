package co.jinear.core.converter.media;

import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.service.media.fileoperation.MediaFileOperationServiceFactory;
import co.jinear.core.service.media.fileoperation.MediaFileOperationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class MediaDtoConverter {

    @Autowired
    protected MediaFileOperationServiceFactory mediaFileOperationServiceFactory;

    public abstract MediaDto map(Media media);

    @AfterMapping
    public void afterMap(@MappingTarget MediaDto mediaDto, Media media) {
        try {
            MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
            String fullPath = mediaFileOperationStrategy.getFullPath(media.getBucketName(), media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey(), media.getOriginalName());
            mediaDto.setUrl(fullPath);
        } catch (Exception e) {
            log.error("After map failed.", e);
        }
    }
}
