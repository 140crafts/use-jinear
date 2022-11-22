package co.jinear.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String root() {
        String greetings = "Hi! ( ˘ ³˘)♥";
        log.info(greetings);
        return greetings;
    }
}