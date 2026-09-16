package co.jinear.core.controller.telemetry;

import co.jinear.core.manager.telemetry.InstanceReportReceiveManager;
import co.jinear.core.model.request.telemetry.InstanceReportRequest;
import co.jinear.core.model.response.telemetry.InstanceReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/instance-report")
public class InstanceReportController {

    private final InstanceReportReceiveManager instanceReportReceiveManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public InstanceReportResponse receive(@Valid @RequestBody InstanceReportRequest instanceReportRequest) {
        return instanceReportReceiveManager.receive(instanceReportRequest);
    }
}
