package co.jinear.core.controller;

import co.jinear.core.manager.calendar.CalendarEventManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final CalendarEventManager calendarEventManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {
    }

    @GetMapping
    public Object debug2(HttpServletResponse response) throws Exception {

        return null;
    }
}
