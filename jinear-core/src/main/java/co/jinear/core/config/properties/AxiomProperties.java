package co.jinear.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class AxiomProperties {

    @Value("${axiom.token}")
    private String token;

    @Value("${axiom.dataset}")
    private String dataset;

    @Value("${axiom.url}")
    private String url;
}
