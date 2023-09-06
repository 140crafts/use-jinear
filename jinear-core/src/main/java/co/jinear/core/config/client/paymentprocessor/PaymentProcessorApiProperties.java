package co.jinear.core.config.client.paymentprocessor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class PaymentProcessorApiProperties {

    @Value("${payment-processor.url}")
    private String paymentProcessorUrl;
    @Value("${payment-processor.security.key}")
    private String paymentProcessorSecurityKey;
    @Value("${payment-processor.security.secret}")
    private String paymentProcessorSecuritySecret;
}
