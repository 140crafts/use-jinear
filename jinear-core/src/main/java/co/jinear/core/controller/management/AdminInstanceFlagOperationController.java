package co.jinear.core.controller.management;

import co.jinear.core.manager.management.InstanceFlagRetrieveManager;
import co.jinear.core.model.request.management.InstanceFlagOperationRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/admin/instance-flag/operation")
public class AdminInstanceFlagOperationController {

    private final InstanceFlagRetrieveManager instanceFlagRetrieveManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse set(@Valid @RequestBody InstanceFlagOperationRequest instanceFlagOperationRequest) {
        return instanceFlagRetrieveManager.set(instanceFlagOperationRequest);
    }
}
