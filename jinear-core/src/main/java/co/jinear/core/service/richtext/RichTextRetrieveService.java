package co.jinear.core.service.richtext;

import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.repository.RichTextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextRetrieveService {

    private final RichTextRepository richTextRepository;
    private final RichTextConverter richTextConverter;

    public RichText retrieveEntity(String richTextId) {
        log.info("Retrieve rich text entity has started. richTextId: {}", richTextId);
        return richTextRepository.findByRichTextIdAndPassiveIdIsNull(richTextId)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<RichTextDto> retrieveIncludingPassivesOptional(String richTextId) {
        log.info("Retrieve optional rich text has started. richTextId: {}", richTextId);
        return richTextRepository.findByRichTextId(richTextId)
                .map(richTextConverter::map);
    }

    public Optional<RichTextDto> retrieveOptional(String richTextId) {
        log.info("Retrieve optional rich text has started. richTextId: {}", richTextId);
        return richTextRepository.findByRichTextIdAndPassiveIdIsNull(richTextId)
                .map(richTextConverter::map);
    }

    public RichTextDto retrieve(String richTextId) {
        log.info("Retrieve rich text has started. richTextId: {}", richTextId);
        return retrieveOptional(richTextId)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<RichTextDto> retrieveByRelatedObject(String relatedObjectId, RichTextType type) {
        log.info("Retrieve by related object has started. relatedObjectId: {}, type: {}", relatedObjectId, type);
        return richTextRepository.findByRelatedObjectIdAndTypeAndPassiveIdIsNull(relatedObjectId, type)
                .map(richTextConverter::map);
    }
}
