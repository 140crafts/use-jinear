package co.jinear.core.manager.richtext;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDetailDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.response.richtext.RichTextVersionPaginatedResponse;
import co.jinear.core.model.response.richtext.RichTextVersionResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.richtext.RichTextRetrieveService;
import co.jinear.core.service.richtext.sync.RichTextSyncAuthResolver;
import co.jinear.core.service.richtext.version.RichTextVersionRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextVersionManager {

    private final SessionInfoService sessionInfoService;
    private final RichTextRetrieveService richTextRetrieveService;
    private final RichTextSyncAuthResolver richTextSyncAuthResolver;
    private final RichTextVersionRetrieveService richTextVersionRetrieveService;

    public RichTextVersionPaginatedResponse retrieveVersions(String richTextId, int page) {
        authorizeRead(richTextId);
        log.info("Retrieve rich text versions has started. richTextId: {}, page: {}", richTextId, page);
        Page<RichTextVersionDto> versions = richTextVersionRetrieveService.retrieveVersions(richTextId, page);
        RichTextVersionPaginatedResponse response = new RichTextVersionPaginatedResponse();
        response.setRichTextVersionDtoPageDto(new PageDto<>(versions));
        return response;
    }

    public RichTextVersionResponse retrieveVersion(String richTextId, String richTextVersionId) {
        authorizeRead(richTextId);
        log.info("Retrieve rich text version has started. richTextId: {}, richTextVersionId: {}", richTextId, richTextVersionId);
        RichTextVersionDetailDto version = richTextVersionRetrieveService.retrieve(richTextId, richTextVersionId);
        RichTextVersionResponse response = new RichTextVersionResponse();
        response.setRichTextVersionDetailDto(version);
        return response;
    }

    private void authorizeRead(String richTextId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        RichText richText = richTextRetrieveService.retrieveEntity(richTextId);
        richTextSyncAuthResolver.validateCanRead(currentAccountId, richText);
    }
}
