package co.jinear.core.controller.scheduler;

import co.jinear.core.manager.payments.PaymentsManager;
import co.jinear.core.manager.scheduledjob.ScheduledJobManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/scheduled-jobs")
public class ScheduledJobController {

    private final ScheduledJobManager scheduledJobManager;
    private final PaymentsManager paymentsManager;

    @GetMapping("/expire-temporary-public-media")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public BaseResponse expireTemporaryPublicMedia(@RequestHeader("User-Agent") String userAgent) {
        log.info("Expire temporary public media has started. userAgent: {}", userAgent);
        scheduledJobManager.updateAllTemporaryPublicMedia();
        return new BaseResponse();
    }

    @GetMapping("/sync-payments")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public BaseResponse retrieveAndApplyLatestPayments(@RequestHeader("User-Agent") String userAgent) {
        log.info("Retrieve and apply latest payments has started. userAgent: {}", userAgent);
        return paymentsManager.retrieveAndApplyLatestPayments();
    }
}
