package co.jinear.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class SignInWithAppleProperties {

    @Value("${signinwithapple.appleAuthBase}")
    private String appleAuthBase;

    @Value("${signinwithapple.keyId}")
    private String keyId;

    @Value("${signinwithapple.keyPath}")
    private String keyPath;

    @Value("${signinwithapple.teamId}")
    private String teamId;

    @Value("${signinwithapple.clientId}")
    private String clientId;

    @Value("${signinwithapple.webClientId}")
    private String webClientId;

    @Value("${signinwithapple.webRedirectUrl}")
    private String webRedirectUrl;

}
