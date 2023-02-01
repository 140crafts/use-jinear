package co.jinear.core.converter.media;

import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.entity.media.Media;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaDtoConverter {

    MediaDto map(Media media);
}
