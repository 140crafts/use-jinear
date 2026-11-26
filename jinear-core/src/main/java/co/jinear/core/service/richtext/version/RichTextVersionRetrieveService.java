package co.jinear.core.service.richtext.version;

import co.jinear.core.converter.richtext.RichTextVersionDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.richtext.RichTextVersionDetailDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDto;
import co.jinear.core.repository.richtext.RichTextVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextVersionRetrieveService {

    private static final int PAGE_SIZE = 50;

    private final RichTextVersionRepository richTextVersionRepository;
    private final RichTextVersionDtoConverter richTextVersionDtoConverter;

    public Page<RichTextVersionDto> retrieveVersions(String richTextId, int page) {
        log.info("Retrieve rich text versions has started. richTextId: {}, page: {}", richTextId, page);
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        return richTextVersionRepository.findAllByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(richTextId, pageRequest)
                .map(richTextVersionDtoConverter::convert);
    }

    public RichTextVersionDetailDto retrieve(String richTextId, String richTextVersionId) {
        log.info("Retrieve rich text version has started. richTextId: {}, richTextVersionId: {}", richTextId, richTextVersionId);
        return richTextVersionRepository.findByRichTextVersionIdAndRichTextIdAndPassiveIdIsNull(richTextVersionId, richTextId)
                .map(richTextVersionDtoConverter::convertDetail)
                .orElseThrow(NotFoundException::new);
    }
}
