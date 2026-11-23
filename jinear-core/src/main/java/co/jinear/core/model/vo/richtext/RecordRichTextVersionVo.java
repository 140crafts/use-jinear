package co.jinear.core.model.vo.richtext;

import co.jinear.core.model.enumtype.richtext.RichTextType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordRichTextVersionVo {
    private String richTextId;
    private RichTextType type;
    private String accountId;
    @ToString.Exclude
    private String html;
    private String title;
}
