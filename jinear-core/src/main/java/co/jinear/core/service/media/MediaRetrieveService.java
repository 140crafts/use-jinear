package co.jinear.core.service.media;

import co.jinear.core.converter.media.MediaDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaRetrieveService {

    private final MediaRepository mediaRepository;
    private final MediaDtoConverter mediaDtoConverter;

    public MediaDto retrieveProfilePicture(String relatedObjectId) {
        log.info("Retrieve profile picture has started. relatedObjectId: {}", relatedObjectId);
        return mediaRepository.findFirstByRelatedObjectIdAndFileTypeAndPassiveIdIsNull(relatedObjectId, FileType.PROFILE_PIC)
                .map(mediaDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<MediaDto> retrieveProfilePictureOptional(String relatedObjectId) {
        log.info("Retrieve profile picture optional has started. relatedObjectId: {}", relatedObjectId);
        return mediaRepository.findFirstByRelatedObjectIdAndFileTypeAndPassiveIdIsNull(relatedObjectId, FileType.PROFILE_PIC)
                .map(mediaDtoConverter::map);
    }
}
