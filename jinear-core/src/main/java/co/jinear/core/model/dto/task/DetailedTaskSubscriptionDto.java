package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.AccountDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetailedTaskSubscriptionDto extends BaseDto {

    private String taskSubscriptionId;
    private String taskId;
    private String accountId;
    private AccountDto accountDto;
}
