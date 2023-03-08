package co.jinear.core.controller.reminder;

import co.jinear.core.manager.reminder.ReminderProcessManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/reminder/process")
public class ReminderProcessController {

    private final ReminderProcessManager reminderProcessManager;

    @GetMapping("/upcoming")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('reminders:process')")
    public BaseResponse processUpcoming(@RequestHeader("User-Agent") String userAgent) {
        log.info("Process upcoming reminder controller triggered from user agent: {}", userAgent);
//        return reminderProcessManager.remindUpcomingJobs();
        return new BaseResponse();
    }

}
