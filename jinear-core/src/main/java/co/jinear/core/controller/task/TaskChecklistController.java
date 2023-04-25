package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.model.request.task.InitializeChecklistRequest;
import co.jinear.core.model.request.task.UpdateChecklistTitleRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.RetrieveChecklistResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/checklist")
public class TaskChecklistController {

    private final TaskChecklistManager taskChecklistManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeChecklist(@Valid @RequestBody InitializeChecklistRequest initializeChecklistRequest) {
        return taskChecklistManager.initializeChecklist(initializeChecklistRequest);
    }

    @GetMapping("/{checklistId}")
    @ResponseStatus(HttpStatus.OK)
    public RetrieveChecklistResponse retrieveChecklist(@PathVariable String checklistId) {
        return taskChecklistManager.retrieve(checklistId);
    }

    @PutMapping("/{checklistId}/label")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateChecklistLabel(@PathVariable String checklistId,
                                             @Valid @RequestBody UpdateChecklistTitleRequest updateChecklistTitleRequest) {
        return taskChecklistManager.updateChecklistLabel(checklistId, updateChecklistTitleRequest);
    }

    @DeleteMapping("/{checklistId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse passivizeChecklist(@PathVariable String checklistId) {
        return taskChecklistManager.passivizeChecklist(checklistId);
    }
}
