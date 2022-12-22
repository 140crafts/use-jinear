package co.jinear.core.service.richtext;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.repository.RichTextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextRetrieveService {

    private final RichTextRepository richTextRepository;
    private final ModelMapper modelMapper;

    public RichText retrieveEntity(String richTextId) {
        log.info("Retrieve rich text entity has started. richTextId: {}", richTextId);
        return richTextRepository.findByRichTextIdAndPassiveIdIsNull(richTextId)
                .orElseThrow(NotFoundException::new);
    }

}
