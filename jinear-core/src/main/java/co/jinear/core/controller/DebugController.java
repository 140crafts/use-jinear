package co.jinear.core.controller;

import co.jinear.core.model.vo.captcha.CaptchaResolveVo;
import co.jinear.core.service.captcha.CaptchaChallengeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/debug")
@RequiredArgsConstructor
public class DebugController {

    private final CaptchaChallengeService captchaChallengeService;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity, @RequestParam("file") MultipartFile file) throws Exception {

    }

    @GetMapping
    public Object debug2(HttpServletResponse response) {
        try {
            System.out.println(captchaChallengeService.verifySolution("test", List.of(new CaptchaResolveVo("a912004578",4049), new CaptchaResolveVo("60d633af8f",34757))));
        } catch (Exception e) {

        }
        return captchaChallengeService.initialize("test");
    }
}
