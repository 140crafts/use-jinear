package co.jinear.core.config.client.expo;

import co.jinear.core.config.interceptor.GenericClientLoggingInterceptor;
import co.jinear.core.config.interceptor.GenericResponseErrorHandler;
import co.jinear.core.config.properties.ExpoProperties;
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
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ExpoClientConfig {

    private final ExpoProperties expoProperties;

    @Bean("expoRestTemplate")
    public RestTemplate expoRestTemplate(UriTemplateHandler expoApiUriTemplateHandler,
                                         @Autowired GenericClientLoggingInterceptor genericClientLoggingInterceptor,
                                         @Autowired GenericResponseErrorHandler genericResponseErrorHandler) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        restTemplate.setUriTemplateHandler(expoApiUriTemplateHandler);
        restTemplate.setInterceptors(List.of(genericClientLoggingInterceptor));
        restTemplate.setErrorHandler(genericResponseErrorHandler);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public UriTemplateHandler expoApiUriTemplateHandler() {
        return new DefaultUriBuilderFactory(expoProperties.getUrl());
    }
}
