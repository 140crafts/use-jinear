package co.jinear.core.config.client.google.auth;

import co.jinear.core.config.interceptor.GenericClientLoggingInterceptor;
import co.jinear.core.config.interceptor.GenericResponseErrorHandler;
import co.jinear.core.config.properties.GCloudProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GoogleAuthClientConfig {

    private final GCloudProperties gCloudProperties;

    @Bean("googleAuthRestTemplate")
    public RestTemplate googleAuthRestTemplate(UriTemplateHandler googleAuthUriTemplateHandler,
                                               @Autowired GenericClientLoggingInterceptor genericClientLoggingInterceptor,
                                               GenericResponseErrorHandler genericResponseErrorHandler) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        restTemplate.setUriTemplateHandler(googleAuthUriTemplateHandler);
        restTemplate.setInterceptors(List.of(genericClientLoggingInterceptor));
        restTemplate.setErrorHandler(genericResponseErrorHandler);
        return restTemplate;
    }

    @Bean
    public UriTemplateHandler googleAuthUriTemplateHandler() {
        return new DefaultUriBuilderFactory(gCloudProperties.getOauthBaseUrl());
    }
}
