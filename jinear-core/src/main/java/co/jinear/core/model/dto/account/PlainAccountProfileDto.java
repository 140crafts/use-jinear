package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class PlainAccountProfileDto extends BaseDto {
    private String accountId;
    private String email;
    private String username;
    private LocaleType localeType;
    private String timeZone;
    @Nullable
    private AccessibleMediaDto profilePicture;
}
