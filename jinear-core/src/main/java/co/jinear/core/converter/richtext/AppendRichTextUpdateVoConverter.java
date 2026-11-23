package co.jinear.core.converter.richtext;

import co.jinear.core.model.request.richtext.AppendRichTextUpdateRequest;
import co.jinear.core.model.vo.richtext.AppendRichTextUpdateVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppendRichTextUpdateVoConverter {

    AppendRichTextUpdateVo convert(AppendRichTextUpdateRequest request, String richTextId, String accountId);
}
