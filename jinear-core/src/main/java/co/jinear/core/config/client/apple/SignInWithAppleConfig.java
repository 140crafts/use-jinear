package co.jinear.core.config.client.apple;

import co.jinear.core.config.interceptor.GenericClientLoggingInterceptor;
import co.jinear.core.config.interceptor.GenericResponseErrorHandler;
import co.jinear.core.config.properties.SignInWithAppleProperties;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "signinwithapple.enabled", havingValue = "true")
@PropertySource("classpath:application.properties")
public class SignInWithAppleConfig {

    private final SignInWithAppleProperties signInWithAppleProperties;

    @Bean
    public PrivateKey signInWithApplePrivateKey() {
        String keyPath = signInWithAppleProperties.getKeyPath();
        try (PEMParser pemParser = new PEMParser(new FileReader(keyPath))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            return converter.getPrivateKey(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean("signInWithAppleRestTemplate")
    public RestTemplate signInWithAppleRestTemplate(UriTemplateHandler signInWithAppleApiUriTemplateHandler,
                                                    @Autowired GenericClientLoggingInterceptor genericClientLoggingInterceptor,
                                                    @Autowired GenericResponseErrorHandler genericResponseErrorHandler) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        restTemplate.setUriTemplateHandler(signInWithAppleApiUriTemplateHandler);
        restTemplate.setInterceptors(List.of(genericClientLoggingInterceptor));
        restTemplate.setErrorHandler(genericResponseErrorHandler);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public UriTemplateHandler signInWithAppleApiUriTemplateHandler() {
        return new DefaultUriBuilderFactory(signInWithAppleProperties.getAppleAuthBase());
    }
}
