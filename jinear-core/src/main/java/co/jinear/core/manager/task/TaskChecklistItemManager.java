package co.jinear.core.manager.task;

import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.ChecklistItem;
import co.jinear.core.model.request.task.ChecklistItemLabelRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.task.InitializeChecklistItemVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.checklist.ChecklistItemService;
import co.jinear.core.service.task.checklist.ChecklistRetrieveService;
import co.jinear.core.validator.task.ChecklistAccessValidator;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskChecklistItemManager {

    private final SessionInfoService sessionInfoService;
    private final ChecklistItemService checklistItemService;
    private final ChecklistRetrieveService checklistRetrieveService;
    private final PassiveService passiveService;
    private final ChecklistAccessValidator checklistAccessValidator;
    private final TaskActivityService taskActivityService;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskAccessValidator taskAccessValidator;

    public BaseResponse initializeChecklistItem(ChecklistItemLabelRequest checklistItemLabelRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Initialize checklist item has started. currentAccountId: {}", currentAccountId);
        ChecklistDto checklistDto = checklistRetrieveService.retrieve(checklistItemLabelRequest.getChecklistId());
        TaskDto taskDto = taskRetrieveService.retrievePlain(checklistDto.getTaskId());
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        InitializeChecklistItemVo initializeChecklistItemVo = mapInitializeVo(checklistItemLabelRequest.getLabel(), checklistDto);
        ChecklistItemDto checklistItemDto = checklistItemService.initialize(initializeChecklistItemVo);
        taskActivityService.initializeChecklistItemInitializedActivity(currentAccountId, taskDto, checklistItemDto.getChecklistItemId());
        return new BaseResponse();
    }

    public BaseResponse updateCheckedStatus(String checklistItemId, boolean checked) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChecklistItem checklistItem = checklistItemService.retrieveEntity(checklistItemId);
        ChecklistDto checklistDto = checklistRetrieveService.retrieve(checklistItem.getChecklistId());
        TaskDto taskDto = taskRetrieveService.retrievePlain(checklistDto.getTaskId());
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Update checklist item checked status has started. checklistItemId: {}, checked: {}", checklistItemId, checked);
        checklistItemService.updateCheckState(checklistItemId, checked);
        taskActivityService.initializeChecklistItemCheckedStatusChangedActivity(currentAccountId, taskDto, checklistItem.getChecklistItemId(), checklistItem.getIsChecked(), checked);
        return new BaseResponse();
    }

    public BaseResponse updateLabel(String checklistItemId, ChecklistItemLabelRequest checklistItemLabelRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChecklistItem checklistItem = checklistItemService.retrieveEntity(checklistItemId);
        ChecklistDto checklistDto = checklistRetrieveService.retrieve(checklistItem.getChecklistId());
        TaskDto taskDto = taskRetrieveService.retrievePlain(checklistDto.getTaskId());
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Update checklist item label has started. checklistItemId: {}, checklistItemLabelRequest: {}", checklistItemId, checklistItemLabelRequest);
        checklistItemService.updateLabel(checklistItemId, checklistItemLabelRequest.getLabel());
        taskActivityService.initializeChecklistItemLabelChangedActivity(currentAccountId, taskDto, checklistItem.getChecklistItemId(), checklistItem.getLabel(), checklistItemLabelRequest.getLabel());
        return new BaseResponse();
    }

    public BaseResponse passivizeChecklistItem(String checklistItemId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ChecklistItem checklistItem = checklistItemService.retrieveEntity(checklistItemId);
        ChecklistDto checklistDto = checklistRetrieveService.retrieve(checklistItem.getChecklistId());
        TaskDto taskDto = taskRetrieveService.retrievePlain(checklistDto.getTaskId());
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Passivize checklist item has started. checklistItemId: {}", checklistItemId);
        String passiveId = checklistItemService.passivizeChecklistItem(checklistItemId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        taskActivityService.initializeChecklistItemRemovedActivity(currentAccountId, taskDto, checklistItem.getChecklistItemId());
        return new BaseResponse();
    }

    private InitializeChecklistItemVo mapInitializeVo(String label, ChecklistDto checklistDto) {
        InitializeChecklistItemVo initializeChecklistItemVo = new InitializeChecklistItemVo();
        initializeChecklistItemVo.setChecklistId(checklistDto.getChecklistId());
        initializeChecklistItemVo.setLabel(label);
        return initializeChecklistItemVo;
    }
}
