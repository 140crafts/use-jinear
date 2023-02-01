package co.jinear.core.service.richtext;

import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextSourceStack;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.richtext.UpdateRichTextVo;
import co.jinear.core.repository.RichTextRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.jinear.core.model.enumtype.richtext.RichTextSourceStack.WYSIWYG;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextInitializeService {

    private static final RichTextSourceStack ACTIVE_STACK = WYSIWYG;

    private final RichTextRepository richTextRepository;
    private final RichTextRetrieveService richTextRetrieveService;
    private final PassiveService passiveService;
    private final RichTextConverter richTextConverter;

    public RichTextDto initializeRichText(InitializeRichTextVo initializeRichTextVo) {
        log.info("Initialize rich text has started. initializeRichTextVo: {}", initializeRichTextVo);
        RichText richText = richTextConverter.map(initializeRichTextVo);
        richText.setSourceStack(ACTIVE_STACK);
        RichText saved = richTextRepository.save(richText);
        return richTextConverter.map(saved);
    }

    public RichTextDto historicallyUpdateRichTextBody(UpdateRichTextVo updateRichTextVo) {
        log.info("Historically update rich text has started. updateRichTextVo: {}", updateRichTextVo);
        RichText richText = richTextRetrieveService.retrieveEntity(updateRichTextVo.getRichTextId());
        passivizeRichText(richText);
        InitializeRichTextVo initializeRichTextVo = mapFromOldOne(updateRichTextVo, richText);
        return initializeRichText(initializeRichTextVo);
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
}
