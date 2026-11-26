package co.jinear.core.config.client.paymentprocessor;

import co.jinear.core.service.client.paymentprocessor.PaymentProcessorHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorApiClientConfig {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(60);

    private final PaymentProcessorApiProperties paymentProcessorApiProperties;

    @Bean
    @ConditionalOnProperty(value = "payment-processor.mock", havingValue = "false")
    public PaymentProcessorHttpClient paymentProcessorHttpClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder
                .baseUrl(paymentProcessorApiProperties.getPaymentProcessorUrl())
                .requestFactory(paymentProcessorRequestFactory())
                .defaultHeaders(this::addBasicAuth)
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PaymentProcessorHttpClient.class);
    }

    private SimpleClientHttpRequestFactory paymentProcessorRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return requestFactory;
    }

    private void addBasicAuth(HttpHeaders headers) {
        headers.setBasicAuth(paymentProcessorApiProperties.getPaymentProcessorSecurityKey(), paymentProcessorApiProperties.getPaymentProcessorSecuritySecret());
    }
}
