package co.jinear.core.manager.task;

import co.jinear.core.converter.task.InitializeChecklistVoConverter;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.request.task.InitializeChecklistRequest;
import co.jinear.core.model.request.task.UpdateChecklistTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.RetrieveChecklistResponse;
import co.jinear.core.model.vo.task.InitializeChecklistVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.checklist.ChecklistRetrieveService;
import co.jinear.core.service.task.checklist.ChecklistService;
import co.jinear.core.validator.task.ChecklistAccessValidator;
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
    private final ChecklistAccessValidator checklistAccessValidator;

    public BaseResponse initializeChecklist(InitializeChecklistRequest initializeChecklistRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, initializeChecklistRequest.getTaskId());
        log.info("Initialize checklist has started. currentAccountId: {}", currentAccountId);
        InitializeChecklistVo initializeChecklistVo = initializeChecklistVoConverter.convert(currentAccountId, initializeChecklistRequest);
        checklistService.initializeChecklist(initializeChecklistVo);
        return new BaseResponse();
    }

    public RetrieveChecklistResponse retrieve(String checklistId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        checklistAccessValidator.validateHasChecklistAccess(currentAccountId, checklistId);
        log.info("Retrieve checklist with task id has started. currentAccountId: {}", currentAccountId);
        ChecklistDto checklistDto = checklistRetrieveService.retrieve(checklistId);
        return mapResponse(checklistDto);
    }

    public BaseResponse passivizeChecklist(String checklistId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        checklistAccessValidator.validateHasChecklistAccess(currentAccountId, checklistId);
        log.info("Passivize checklist has started. currentAccountId: {}", currentAccountId);
        String passiveId = checklistService.passivizeChecklist(checklistId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse updateChecklistLabel(String checklistId, UpdateChecklistTitleRequest updateChecklistTitleRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        checklistAccessValidator.validateHasChecklistAccess(currentAccountId, checklistId);
        log.info("Update checklist title has started. currentAccountId: {}", currentAccountId);
        checklistService.updateChecklistTitle(checklistId, updateChecklistTitleRequest.getTitle());
        return new BaseResponse();
    }

    private RetrieveChecklistResponse mapResponse(ChecklistDto checklistDto) {
        RetrieveChecklistResponse retrieveChecklistResponse = new RetrieveChecklistResponse();
        retrieveChecklistResponse.setChecklistDto(checklistDto);
        return retrieveChecklistResponse;
    }
}
