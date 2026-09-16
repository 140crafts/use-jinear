package co.jinear.core.config.client.telemetry;

import co.jinear.core.config.interceptor.GenericResponseErrorHandler;
import co.jinear.core.config.properties.TelemetryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class TelemetryClientConfig {

    private static final int CONNECT_TIMEOUT_MILLIS = 5000;
    private static final int READ_TIMEOUT_MILLIS = 10000;

    private final TelemetryProperties telemetryProperties;

    @Bean("telemetryRestTemplate")
    public RestTemplate telemetryRestTemplate(UriTemplateHandler telemetryUriTemplateHandler,
                                              @Autowired GenericResponseErrorHandler genericResponseErrorHandler) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        simpleClientHttpRequestFactory.setReadTimeout(READ_TIMEOUT_MILLIS);
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        restTemplate.setUriTemplateHandler(telemetryUriTemplateHandler);
        restTemplate.setErrorHandler(genericResponseErrorHandler);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public UriTemplateHandler telemetryUriTemplateHandler() {
        return new DefaultUriBuilderFactory(telemetryProperties.getUrl());
    }
}
