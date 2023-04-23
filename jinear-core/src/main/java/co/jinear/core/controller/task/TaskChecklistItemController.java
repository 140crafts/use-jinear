package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskChecklistItemManager;
import co.jinear.core.model.request.task.ChecklistItemLabelRequest;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/{taskId}/checklist/item")
public class TaskChecklistItemController {

    private final TaskChecklistItemManager taskChecklistItemManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeChecklistItem(@PathVariable String taskId,
                                                @RequestBody ChecklistItemLabelRequest checklistItemLabelRequest) {
        return taskChecklistItemManager.initializeChecklistItem(taskId, checklistItemLabelRequest);
    }

    @PutMapping("/{checklistItemId}/{checked}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateCheckedStatus(@PathVariable String taskId,
                                            @PathVariable String checklistItemId,
                                            @PathVariable boolean checked) {
        return taskChecklistItemManager.updateCheckedStatus(checklistItemId, checked);
    }

    @PutMapping("/{checklistItemId}/label")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateLabel(@PathVariable String taskId,
                                    @PathVariable String checklistItemId,
                                    @RequestBody ChecklistItemLabelRequest checklistItemLabelRequest) {
        return taskChecklistItemManager.updateLabel(checklistItemId, checklistItemLabelRequest);
    }

    @DeleteMapping("/{checklistItemId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteChecklistItem(@PathVariable String taskId,
                                            @PathVariable String checklistItemId) {
        return taskChecklistItemManager.passivizeChecklistItem(checklistItemId);
    }

}
