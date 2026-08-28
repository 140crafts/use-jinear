package co.jinear.core.controller.management;

import co.jinear.core.manager.management.AdminAccountManager;
import co.jinear.core.model.request.management.AdminAccountCreateRequest;
import co.jinear.core.model.request.management.AdminAccountPasswordUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountRetrieveResponse;
import co.jinear.core.model.response.management.AdminAccountListingResponse;
import co.jinear.core.model.response.team.TeamMembershipsResponse;
import co.jinear.core.model.response.workspace.AccountWorkspacesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/admin/account")
public class AdminAccountController {

    private final AdminAccountManager adminAccountManager;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public AdminAccountListingResponse retrieveAllAccounts(@RequestParam(required = false, defaultValue = "0") Integer page) {
        return adminAccountManager.retrieveAllAccounts(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountRetrieveResponse createAccount(@Valid @RequestBody AdminAccountCreateRequest adminAccountCreateRequest) {
        return adminAccountManager.createAccount(adminAccountCreateRequest);
    }

    @PutMapping("/{accountId}/password")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updatePassword(@PathVariable String accountId,
                                       @Valid @RequestBody AdminAccountPasswordUpdateRequest adminAccountPasswordUpdateRequest) {
        return adminAccountManager.updatePassword(accountId, adminAccountPasswordUpdateRequest);
    }

    @GetMapping("/{accountId}/workspaces")
    @ResponseStatus(HttpStatus.OK)
    public AccountWorkspacesResponse retrieveWorkspaceMemberships(@PathVariable String accountId) {
        return adminAccountManager.retrieveWorkspaceMemberships(accountId);
    }

    @GetMapping("/{accountId}/teams")
    @ResponseStatus(HttpStatus.OK)
    public TeamMembershipsResponse retrieveTeamMemberships(@PathVariable String accountId) {
        return adminAccountManager.retrieveTeamMemberships(accountId);
    }
}
