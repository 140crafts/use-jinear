package co.jinear.core.controller.management;

import co.jinear.core.manager.management.InstanceInfoRetrieveManager;
import co.jinear.core.model.response.management.InstanceInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/admin/instance-info")
public class AdminInstanceInfoController {

    private final InstanceInfoRetrieveManager instanceInfoRetrieveManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public InstanceInfoResponse retrieve() {
        return instanceInfoRetrieveManager.retrieve();
    }
}
