package co.jinear.core.validator.task;

import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.service.task.checklist.ChecklistRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChecklistAccessValidator {

    private final ChecklistRetrieveService checklistRetrieveService;
    private final TaskAccessValidator taskAccessValidator;

    public void validateHasChecklistAccess(String accountId, String checklistId) {
        log.info("Validate has checklist access has started.");
        ChecklistDto checklistDto = checklistRetrieveService.retrieve(checklistId);
        taskAccessValidator.validateTaskAccess(accountId, checklistDto.getTaskId());
    }
}
