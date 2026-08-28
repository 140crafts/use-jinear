package co.jinear.core.controller.management;

import co.jinear.core.manager.management.InstanceFlagRetrieveManager;
import co.jinear.core.model.response.management.InstanceFlagListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/instance-flag")
public class InstanceFlagListingController {

    private final InstanceFlagRetrieveManager instanceFlagRetrieveManager;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public InstanceFlagListingResponse list() {
        return instanceFlagRetrieveManager.retrieveAll();
    }
}
