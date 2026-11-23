package co.jinear.core.model.dto.richtext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RichTextVersionDetailDto extends RichTextVersionDto {

    @ToString.Exclude
    private String html;
    private String title;
}
