package co.jinear.core.service.richtext.sync;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDetailRichTextSyncAuthStrategy implements RichTextSyncAuthStrategy {

    private final TaskRetrieveService taskRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;

    @Override
    public RichTextType supports() {
        return RichTextType.TASK_DETAIL;
    }

    @Override
    public void validateCanRead(String accountId, RichText richText) {
        validateTaskAccess(accountId, richText);
    }

    @Override
    public void validateCanWrite(String accountId, RichText richText) {
        validateTaskAccess(accountId, richText);
    }

    private void validateTaskAccess(String accountId, RichText richText) {
        TaskDto taskDto = taskRetrieveService.retrieve(richText.getRelatedObjectId());
        workspaceValidator.validateHasAccess(accountId, taskDto.getWorkspaceId());
        teamAccessValidator.validateTeamAccess(accountId, taskDto.getTeamId());
    }
}
