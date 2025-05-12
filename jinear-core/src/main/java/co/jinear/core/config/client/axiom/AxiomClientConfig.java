package co.jinear.core.config.client.axiom;

import co.jinear.core.config.interceptor.GenericResponseErrorHandler;
import co.jinear.core.config.properties.AxiomProperties;
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
public class AxiomClientConfig {

    private final AxiomProperties axiomProperties;

    @Bean("axiomRestTemplate")
    public RestTemplate axiomRestTemplate(UriTemplateHandler axiomApiUriTemplateHandler,
                                          @Autowired GenericResponseErrorHandler genericResponseErrorHandler) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        restTemplate.setUriTemplateHandler(axiomApiUriTemplateHandler);
        restTemplate.setErrorHandler(genericResponseErrorHandler);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public UriTemplateHandler axiomApiUriTemplateHandler() {
        return new DefaultUriBuilderFactory(axiomProperties.getUrl());
    }
}
