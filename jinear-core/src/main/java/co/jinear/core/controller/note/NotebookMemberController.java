package co.jinear.core.controller.note;

import co.jinear.core.manager.notebook.NotebookMemberManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.notebook.NotebookMemberPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/notebook/member")
public class NotebookMemberController {

    private final NotebookMemberManager notebookMemberManager;

    @GetMapping("/{notebookId}")
    @ResponseStatus(HttpStatus.OK)
    public NotebookMemberPaginatedResponse listMembers(@PathVariable String notebookId,
                                                       @RequestParam(required = false, defaultValue = "0") Integer page) {
        return notebookMemberManager.listMembers(notebookId, page);
    }

    @PostMapping("/{notebookId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse addMember(@PathVariable String notebookId,
                                  @PathVariable String accountId) {
        return notebookMemberManager.addMember(notebookId, accountId);
    }

    @DeleteMapping("/{notebookId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse removeMember(@PathVariable String notebookId,
                                     @PathVariable String accountId) {
        return notebookMemberManager.removeMember(notebookId, accountId);
    }
}
