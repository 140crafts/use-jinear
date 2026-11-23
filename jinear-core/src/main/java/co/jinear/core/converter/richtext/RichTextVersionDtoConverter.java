package co.jinear.core.converter.richtext;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.richtext.RichTextVersionDetailDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDto;
import co.jinear.core.model.entity.richtext.RichTextVersion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class})
public interface RichTextVersionDtoConverter {

    RichTextVersionDto convert(RichTextVersion richTextVersion);

    RichTextVersionDetailDto convertDetail(RichTextVersion richTextVersion);
}
