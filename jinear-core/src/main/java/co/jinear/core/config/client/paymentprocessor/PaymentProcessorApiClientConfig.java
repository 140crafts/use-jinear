package co.jinear.core.config.client.paymentprocessor;

import feign.auth.BasicAuthRequestInterceptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaymentProcessorApiClientConfig {

    private final PaymentProcessorApiProperties paymentProcessorApiProperties;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(paymentProcessorApiProperties.getPaymentProcessorSecurityKey(), paymentProcessorApiProperties.getPaymentProcessorSecuritySecret());
    }
}
