package co.jinear.core.controller;

import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.vo.google.GenerateUserConsentUrlVo;
import co.jinear.core.service.google.GoogleOAuthApiCallerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final GoogleOAuthApiCallerService googleOAuthApiCallerService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {
    }

    @GetMapping
    public String debug2(HttpServletResponse response) throws IOException {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.LOGIN);
        generateUserConsentUrlVo.setIncludeEmailScopes(Boolean.FALSE);
        generateUserConsentUrlVo.setIncludeCalendarScopes(Boolean.FALSE);

        String redirect = googleOAuthApiCallerService.generateUserConsentUrl(generateUserConsentUrlVo);
        log.info("redirect: {}", redirect);
        return redirect;
    }
}
