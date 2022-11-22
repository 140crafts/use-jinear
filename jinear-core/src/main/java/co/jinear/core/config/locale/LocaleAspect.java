package co.jinear.core.config.locale;

import co.jinear.core.model.request.BaseRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Component
@Aspect
public class LocaleAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void allRestControllers() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
    )
    public void mappingAnnotations() {
    }

    @Before(value = "allRestControllers() && mappingAnnotations()")
    public void setLanguage(JoinPoint jp) {
        Optional<String> localeOpt = retrieveLocaleFromRequest(jp);
        localeOpt.ifPresent(locale -> LocaleContextHolder.setLocale(new Locale(locale)));
    }

    private Optional<String> retrieveLocaleFromRequest(JoinPoint jp) {
        return Arrays.stream(jp.getArgs())
                .filter(BaseRequest.class::isInstance)
                .map(BaseRequest.class::cast)
                .map(BaseRequest::getLocale)
                .filter(StringUtils::isNotBlank)
                .findFirst();
    }
}
