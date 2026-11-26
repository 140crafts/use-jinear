package co.jinear.core.model.vo.richtext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppendRichTextUpdateVo {
    private String richTextId;
    private String accountId;
    private String sessionInfoId;
    @ToString.Exclude
    private String update;
    @ToString.Exclude
    private String html;
    private String title;
}
