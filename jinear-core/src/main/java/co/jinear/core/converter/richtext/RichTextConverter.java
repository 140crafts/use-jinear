package co.jinear.core.converter.richtext;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RichTextConverter {

    RichText map(InitializeRichTextVo initializeRichTextVo);

    RichTextDto map(RichText richText);
}
