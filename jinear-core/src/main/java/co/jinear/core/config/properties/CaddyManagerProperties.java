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
public class CaddyManagerProperties {

    @Value("${caddymanager.baseUrl}")
    private String caddyManagerBaseUrl;

    @Value("${caddymanager.token}")
    private String token;
}
