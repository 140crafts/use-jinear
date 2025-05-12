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
public class MessageApiProperties {

    @Value("${message-api.endpoint}")
    private String endpoint;

    @Value("${message-api.token}")
    private String token;

}
