package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialAccessManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.material.MaterialAccessPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/material/access")
public class MaterialAccessController {

    private final MaterialAccessManager materialAccessManager;

    @GetMapping("/{materialId}")
    @ResponseStatus(HttpStatus.OK)
    public MaterialAccessPaginatedResponse accessList(@PathVariable String materialId,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page) {
        return materialAccessManager.accessList(materialId, page);
    }

    @PostMapping("/{materialId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse giveAccess(@PathVariable String materialId,
                                   @PathVariable String accountId) {
        return materialAccessManager.giveAccess(materialId, accountId);
    }

    @DeleteMapping("/{materialId}/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse revokeAccess(@PathVariable String materialId,
                                     @PathVariable String accountId) {
        return materialAccessManager.revokeAccess(materialId, accountId);
    }
}
