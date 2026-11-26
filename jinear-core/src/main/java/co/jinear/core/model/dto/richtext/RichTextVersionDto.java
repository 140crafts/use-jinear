package co.jinear.core.model.dto.richtext;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class RichTextVersionDto extends BaseDto {

    private String richTextVersionId;
    private String richTextId;
    private String accountId;
    @Nullable
    private PlainAccountProfileDto account;
}
