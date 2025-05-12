package co.jinear.core.model.dto.feed;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.AccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedMemberDto extends BaseDto {

    private String feedMemberId;
    private String accountId;
    private String workspaceId;
    private String feedId;
    private AccountDto account;
    private FeedDto feed;
}
