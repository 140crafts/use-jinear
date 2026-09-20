package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCollaboratorDto extends BaseDto {

    private String taskCollaboratorId;
    private String taskId;
    private String accountId;
    private PlainAccountProfileDto collaborator;
}
