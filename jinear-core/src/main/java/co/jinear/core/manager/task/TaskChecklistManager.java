package co.jinear.core.manager.task;

import co.jinear.core.converter.task.InitializeChecklistVoConverter;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.request.task.InitializeChecklistRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.RetrieveChecklistResponse;
import co.jinear.core.model.vo.task.InitializeChecklistVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.checklist.ChecklistRetrieveService;
import co.jinear.core.service.task.checklist.ChecklistService;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskChecklistManager {

    private final SessionInfoService sessionInfoService;
    private final TaskAccessValidator taskAccessValidator;
    private final ChecklistService checklistService;
    private final ChecklistRetrieveService checklistRetrieveService;
    private final InitializeChecklistVoConverter initializeChecklistVoConverter;
    private final PassiveService passiveService;

    public BaseResponse initializeChecklist(String taskId, InitializeChecklistRequest initializeChecklistRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Initialize checklist has started. taskId: {}, initializeChecklistRequest: {}", taskId, initializeChecklistRequest);
        InitializeChecklistVo initializeChecklistVo = initializeChecklistVoConverter.convert(taskId, currentAccountId, initializeChecklistRequest);
        checklistService.initializeChecklist(initializeChecklistVo);
        return new BaseResponse();
    }

    public RetrieveChecklistResponse retrieveWithTaskId(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Retrieve checklist with task id has started. taskId: {}", taskId);
        ChecklistDto checklistDto = checklistRetrieveService.retrieveByTaskId(taskId);
        return mapResponse(checklistDto);
    }

    public BaseResponse passivizeChecklist(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Passivize checklist has started. taskId: {}", taskId);
        String passiveId = checklistService.passivizeChecklist(taskId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    private RetrieveChecklistResponse mapResponse(ChecklistDto checklistDto) {
        RetrieveChecklistResponse retrieveChecklistResponse = new RetrieveChecklistResponse();
        retrieveChecklistResponse.setChecklistDto(checklistDto);
        return retrieveChecklistResponse;
    }
}
