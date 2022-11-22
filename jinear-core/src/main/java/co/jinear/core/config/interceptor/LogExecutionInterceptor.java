package co.jinear.core.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogExecutionInterceptor implements HandlerInterceptor {

    private static final String REQUEST_START_TIME = "requestStartTime";
    private static final long PROCESS_DURATION_ALERT_LIMIT = 4000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (handler instanceof HandlerMethod handlerMethod) {
                request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
                log.info("Starting controller method for {}.{}",
                        handlerMethod.getBeanType().getSimpleName(),
                        handlerMethod.getMethod().getName());
            }
        } catch (Exception e) {
            log.error("Caught an exception while executing handler method", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (handler instanceof HandlerMethod handlerMethod) {
                final Long duration = (System.currentTimeMillis() - (Long) request.getAttribute(REQUEST_START_TIME));
                log.info("Completed controller method for {}.{} takes {} ms",
                        handlerMethod.getBeanType().getSimpleName(),
                        handlerMethod.getMethod().getName(),
                        duration);
                if (PROCESS_DURATION_ALERT_LIMIT <= duration.longValue()) {
                    log.error("[REQUEST_TOOK_TOO_LONG_TO_PROCESS] - Controller method for {}.{} takes too long to process ({} ms)",
                            handlerMethod.getBeanType().getSimpleName(),
                            handlerMethod.getMethod().getName(),
                            duration);
                }
            }
        } catch (Exception e) {
            log.error("Caught an exception while executing handler method", e);
        }
    }
}
