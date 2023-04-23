package co.jinear.core.manager.task;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.entity.task.Checklist;
import co.jinear.core.model.entity.task.ChecklistItem;
import co.jinear.core.model.request.task.ChecklistItemLabelRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.task.InitializeChecklistItemVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.checklist.ChecklistItemService;
import co.jinear.core.service.task.checklist.ChecklistRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskChecklistItemManager {

    private final SessionInfoService sessionInfoService;
    private final TaskAccessValidator taskAccessValidator;
    private final ChecklistItemService checklistItemService;
    private final ChecklistRetrieveService checklistRetrieveService;
    private final PassiveService passiveService;

    public BaseResponse initializeChecklistItem(String taskId, ChecklistItemLabelRequest checklistItemLabelRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Initialize checklist item has started. taskId: {}, checklistItemLabelRequest: {}", taskId, checklistItemLabelRequest);
        ChecklistDto checklistDto = checklistRetrieveService.retrieveByTaskId(taskId);
        InitializeChecklistItemVo initializeChecklistItemVo = mapInitializeVo(checklistItemLabelRequest.getLabel(), checklistDto);
        checklistItemService.initialize(initializeChecklistItemVo);
        return new BaseResponse();
    }

    public BaseResponse updateCheckedStatus(String checklistItemId, boolean checked) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChecklistItem checklistItem = checklistItemService.retrieveEntity(checklistItemId);
        validateChecklistItemAccess(currentAccountId, checklistItem);
        log.info("Update checklist item checked status has started. checklistItemId: {}, checked: {}", checklistItemId, checked);
        checklistItemService.updateCheckState(checklistItemId, checked);
        return new BaseResponse();
    }

    public BaseResponse updateLabel(String checklistItemId, ChecklistItemLabelRequest checklistItemLabelRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChecklistItem checklistItem = checklistItemService.retrieveEntity(checklistItemId);
        validateChecklistItemAccess(currentAccountId, checklistItem);
        log.info("Update checklist item label has started. checklistItemId: {}, checklistItemLabelRequest: {}", checklistItemId, checklistItemLabelRequest);
        checklistItemService.updateLabel(checklistItemId, checklistItemLabelRequest.getLabel());
        return new BaseResponse();
    }

    public BaseResponse passivizeChecklistItem(String checklistItemId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChecklistItem checklistItem = checklistItemService.retrieveEntity(checklistItemId);
        validateChecklistItemAccess(currentAccountId, checklistItem);
        log.info("Passivize checklist item has started. checklistItemId: {}", checklistItemId);
        String passiveId = checklistItemService.passivizeChecklistItem(checklistItemId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    private void validateChecklistItemAccess(String currentAccountId, ChecklistItem checklistItem) {
        String taskId = Optional.of(checklistItem)
                .map(ChecklistItem::getChecklist)
                .map(Checklist::getTaskId)
                .orElseThrow(NoAccessException::new);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
    }

    private InitializeChecklistItemVo mapInitializeVo(String label, ChecklistDto checklistDto) {
        InitializeChecklistItemVo initializeChecklistItemVo = new InitializeChecklistItemVo();
        initializeChecklistItemVo.setChecklistId(checklistDto.getChecklistId());
        initializeChecklistItemVo.setLabel(label);
        return initializeChecklistItemVo;
    }
}
