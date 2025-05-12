package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlainAccountDto extends BaseDto {
    private String accountId;
    private String username;
}
