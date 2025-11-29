package co.jinear.core.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/v1/debug")
@RequiredArgsConstructor
public class DebugController {


    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity, @RequestParam("file") MultipartFile file) throws Exception {

    }

    @GetMapping
    public Object debug2(HttpServletResponse response) {
        return null;
    }
}
