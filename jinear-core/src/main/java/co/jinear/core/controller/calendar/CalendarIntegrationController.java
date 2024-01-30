package co.jinear.core.controller.calendar;

import co.jinear.core.manager.calendar.CalendarIntegrationManager;
import co.jinear.core.model.response.calendar.CalendarSourceListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/calendar/integration")
public class CalendarIntegrationController {

    private final CalendarIntegrationManager calendarIntegrationManager;

    @GetMapping("/sources")
    @ResponseStatus(HttpStatus.OK)
    public CalendarSourceListResponse listIntegrationSources() {
        return calendarIntegrationManager.listIntegrationSources();
    }

}
