package co.jinear.core.config.security;

import co.jinear.core.config.locale.LocaleMessage;
import co.jinear.core.config.locale.MessageSourceLocalizer;
import co.jinear.core.controller.advice.ApiAdviceHelper;
import co.jinear.core.model.response.BaseResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ApiAdviceHelper apiAdviceHelper;
    private final MessageSourceLocalizer messageSourceLocalizer;
    private final Gson gson;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage("common.error.bad-credentials");
        BaseResponse errorBaseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
        response.resetBuffer();
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(errorBaseResponse));
        response.flushBuffer();
    }
}
