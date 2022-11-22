package co.jinear.core.model.vo.account.password;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidatePasswordVo {
    private String accountId;
    @ToString.Exclude
    private String password;
}
