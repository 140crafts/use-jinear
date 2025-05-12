package co.jinear.core.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/robots/debug")
@RequiredArgsConstructor
public class DebugRobotsController {

    @GetMapping
    public Object robotsDebug(HttpServletResponse response) {
        log.info("Robots debug.");
        return null;
    }
}
