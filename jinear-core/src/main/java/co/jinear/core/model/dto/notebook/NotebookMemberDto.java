package co.jinear.core.model.dto.notebook;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class NotebookMemberDto extends BaseDto {

    private String notebookMemberId;
    private String notebookId;
    private String accountId;
    private String workspaceId;
    @Nullable
    private PlainAccountProfileDto account;
}
