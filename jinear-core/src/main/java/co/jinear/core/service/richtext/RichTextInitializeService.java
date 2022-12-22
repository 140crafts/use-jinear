package co.jinear.core.service.richtext;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextSourceStack;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.richtext.UpdateRichTextVo;
import co.jinear.core.repository.RichTextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static co.jinear.core.model.enumtype.richtext.RichTextSourceStack.WYSIWYG;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextInitializeService {

    private static final RichTextSourceStack ACTIVE_STACK=WYSIWYG;

    private final RichTextRepository richTextRepository;
    private final RichTextRetrieveService richTextRetrieveService;
    private final ModelMapper modelMapper;

    public RichTextDto initializeRichText(InitializeRichTextVo initializeRichTextVo) {
        log.info("Initialize rich text has started. initializeRichTextVo: {}", initializeRichTextVo);
        RichText richText = modelMapper.map(initializeRichTextVo, RichText.class);
        richText.setSourceStack(ACTIVE_STACK);
        RichText saved = richTextRepository.save(richText);
        return modelMapper.map(saved, RichTextDto.class);
    }

    public RichTextDto updateRichTextBody(UpdateRichTextVo updateRichTextVo) {
        log.info("Update rich text has started. updateRichTextVo: {}", updateRichTextVo);
        RichText richText = richTextRetrieveService.retrieveEntity(updateRichTextVo.getRichTextId());
        richText.setValue(updateRichTextVo.getValue());
        richText.setType(updateRichTextVo.getType());
        richText.setSourceStack(ACTIVE_STACK);
        RichText saved = richTextRepository.save(richText);
        return modelMapper.map(saved, RichTextDto.class);
    }
}
