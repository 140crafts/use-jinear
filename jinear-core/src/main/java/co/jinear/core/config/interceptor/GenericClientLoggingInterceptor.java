package co.jinear.core.config.interceptor;

import co.jinear.core.model.entity.servicelog.ServiceLog;
import co.jinear.core.service.servicelog.ServiceLogCreateService;
import co.jinear.core.system.util.DateHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericClientLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final ServiceLogCreateService serviceLogCreateService;

    @Override
    public @NonNull ClientHttpResponse intercept(@NonNull HttpRequest request, byte @NonNull [] body, ClientHttpRequestExecution execution) throws IOException {
        ServiceLog serviceLog = new ServiceLog();
        serviceLog.setDirection("OUTBOUND");
        serviceLog.setCreatedDate(DateHelper.now());

        fillServiceLogWithRequest(request, body, serviceLog);
        logRequest(serviceLog);
        ClientHttpResponse response = execution.execute(request, body);
        fillServiceLogWithResponse(response, serviceLog);
        logResponse(serviceLog);
        serviceLogCreateService.create(serviceLog);
        return response;
    }

    private void fillServiceLogWithRequest(HttpRequest request, byte[] body, ServiceLog serviceLog) throws UnknownHostException {
        serviceLog.setUri(request.getURI().toString());
        serviceLog.setMethod(Objects.requireNonNull(request.getMethod()).name());
        serviceLog.setRequestPayload(new String(body));
        serviceLog.setRequestHeaders(request.getHeaders().toString());
        serviceLog.setRequestTime(DateHelper.now());

        InetAddress addr = InetAddress.getLocalHost();
        serviceLog.setClientIp(addr.getHostAddress());
    }

    private void fillServiceLogWithResponse(ClientHttpResponse response, ServiceLog serviceLog) throws IOException {
        serviceLog.setStatusCode(response.getStatusCode().value());
        serviceLog.setStatusText(response.getStatusText());
        serviceLog.setResponsePayload(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        serviceLog.setResponseHeaders(response.getHeaders().toString());
        serviceLog.setResponseTime(DateHelper.now());
    }

    private void logRequest(ServiceLog serviceLog) {
        String message = """
                ===========================request begin================================================
                URI         : %s
                Method      : %s
                Headers     : %s
                ==========================request end================================================
                """.formatted(serviceLog.getUri(), serviceLog.getMethod(), serviceLog.getRequestHeaders());
        log.debug(message);

    }

    private void logResponse(ServiceLog serviceLog) {
        String message = """
                ============================response begin==========================================
                Status code      : %s
                Status text      : %s
                Headers          : %s
                =======================response end=================================================
                """.formatted(serviceLog.getStatusCode(), serviceLog.getStatusText(), serviceLog.getResponseHeaders());
        log.debug(message);
    }
}
