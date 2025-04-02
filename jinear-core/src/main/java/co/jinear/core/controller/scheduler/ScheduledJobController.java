package co.jinear.core.controller.scheduler;

import co.jinear.core.manager.payments.PaymentsManager;
import co.jinear.core.manager.scheduledjob.ScheduledJobManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/scheduled-jobs")
public class ScheduledJobController {

    private final ScheduledJobManager scheduledJobManager;
    private final PaymentsManager paymentsManager;
//
//     MOVED TO MANAGER LEVEL SCHEDULED.
//
//    @GetMapping("/expire-temporary-public-media")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ROLE_SERVICE')")
//    public BaseResponse expireTemporaryPublicMedia(@RequestHeader("User-Agent") String userAgent) {
//        log.info("Expire temporary public media has started. userAgent: {}", userAgent);
//        scheduledJobManager.updateAllTemporaryPublicMedia();
//        return new BaseResponse();
//    }
//
//    @GetMapping("/sync-payments")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ROLE_SERVICE')")
//    public BaseResponse retrieveAndApplyLatestPayments(@RequestHeader("User-Agent") String userAgent) {
//        log.info("Retrieve and apply latest payments has started. userAgent: {}", userAgent);
//        return paymentsManager.retrieveAndApplyLatestPayments();
//    }
//
//    @GetMapping("/sync-custom-project-domains")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ROLE_SERVICE')")
//    public BaseResponse syncCustomProjectDomains(@RequestHeader("User-Agent") String userAgent) {
//        log.info("Sync custom project domains has started. userAgent: {}", userAgent);
//        scheduledJobManager.checkAndSyncCustomDomains();
//        return new BaseResponse();
//    }
}
