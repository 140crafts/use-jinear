package co.jinear.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class HomeController {

    private static final String greetings = "Hi! ( ˘ ³˘)♥";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map root() {
        return Map.of("message", greetings);
    }
}