package co.jinear.core.service.richtext;

import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextSourceStack;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.richtext.UpdateRichTextVo;
import co.jinear.core.repository.RichTextRepository;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static co.jinear.core.model.enumtype.richtext.RichTextSourceStack.RC;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextInitializeService {

    private static final String RICH_TEXT_URL_PATTERN = "/FILE_STORAGE/RICH_TEXT/";
    private static final RichTextSourceStack ACTIVE_STACK = RC;

    private final RichTextRepository richTextRepository;
    private final RichTextRetrieveService richTextRetrieveService;
    private final PassiveService passiveService;
    private final RichTextConverter richTextConverter;
    private final HtmlSanitizeService htmlSanitizeService;
    private final MediaOperationService mediaOperationService;

    public RichTextDto initializeRichText(InitializeRichTextVo initializeRichTextVo) {
        log.info("Initialize rich text has started. initializeRichTextVo: {}", initializeRichTextVo);
        RichText richText = richTextConverter.map(initializeRichTextVo);
        sanitizeValue(richText);
        richText.setSourceStack(ACTIVE_STACK);
        RichText saved = richTextRepository.save(richText);
        extractImagesAndAssignRichTextOwnership(saved.getValue(), saved.getRichTextId());
        return richTextConverter.map(saved);
    }

    public RichTextDto historicallyUpdateRichTextBody(UpdateRichTextVo updateRichTextVo) {
        log.info("Historically update rich text has started. updateRichTextVo: {}", updateRichTextVo);
        RichText richText = richTextRetrieveService.retrieveEntity(updateRichTextVo.getRichTextId());
        passivizeRichText(richText);
        InitializeRichTextVo initializeRichTextVo = mapFromOldOne(updateRichTextVo, richText);
        //todo check diff and delete removed images
        return initializeRichText(initializeRichTextVo);
    }

    public void updateRelatedObjectId(String richTextId, String relatedObjectId) {
        log.info("Update rich text related object id has started. richTextId: {}, relatedObjectId: {}", richTextId, relatedObjectId);
        RichText richText = richTextRetrieveService.retrieveEntity(richTextId);
        richText.setRelatedObjectId(relatedObjectId);
        richTextRepository.save(richText);
    }

    private void passivizeRichText(RichText richText) {
        final String richTextId = richText.getRichTextId();
        log.info("Passivize rich text has started for richTextId: {}", richTextId);
        String passiveId = passiveService.createSystemActionPassive();
        richText.setPassiveId(passiveId);
        richTextRepository.save(richText);
        log.info("Passivize rich text has ended for richTextId: {}, passiveId: {}", richTextId, passiveId);
    }

    private InitializeRichTextVo mapFromOldOne(UpdateRichTextVo updateRichTextVo, RichText richText) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(richText.getRelatedObjectId());
        initializeRichTextVo.setType(richText.getType());
        initializeRichTextVo.setValue(updateRichTextVo.getValue());
        return initializeRichTextVo;
    }

    private void sanitizeValue(RichText richText) {
        richText.setValue(htmlSanitizeService.sanitizeHTML(richText.getValue()));
    }

    private void extractImagesAndAssignRichTextOwnership(String html, String richTextId) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("img");
        elements.forEach(element -> extractSrcAndUpdate(richTextId, element));
    }

    private void extractSrcAndUpdate(String richTextId, Element element) {
        Attribute src = element.attribute("src");
        String srcUrl = src.getValue();
        checkUrlPatternAndUpdate(richTextId, srcUrl);
    }

    private void checkUrlPatternAndUpdate(String richTextId, String srcUrl) {
        if (srcUrl.contains(RICH_TEXT_URL_PATTERN)) {
            parseFieldsAndUpdateMediaEntity(richTextId, srcUrl);
        }
    }

    private void parseFieldsAndUpdateMediaEntity(String richTextId, String srcUrl) {
        try {
            String[] split = srcUrl.split(Pattern.quote(RICH_TEXT_URL_PATTERN));
            String detail = split[1];
            String[] detailSplit = detail.split(Pattern.quote("/"));
            String relatedObjectId = detailSplit[0];
            String mediaKey = detailSplit[2];
            mediaOperationService.updateLateOwnerIdAndOwnershipStatus(relatedObjectId, mediaKey, richTextId);
        } catch (Exception e) {
            log.error("Rich text image update late owner and ownership status has failed.", e);
        }
    }
}
