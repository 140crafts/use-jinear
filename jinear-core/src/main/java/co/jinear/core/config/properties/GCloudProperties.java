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
public class GCloudProperties {

    @Value("${gcloud.projectId}")
    private String projectId;

    @Value("${gcloud.cloudStorage.bucketName}")
    private String bucketName;

    @Value("${gcloud.oauth.baseUrl}")
    private String oauthBaseUrl;

    @Value("${gcloud.oauth.clientId}")
    private String oauthClientId;

    @Value("${gcloud.oauth.clientSecret}")
    private String oauthClientSecret;

    @Value("${gcloud.oauth.redirect-url.login}")
    private String loginRedirectUrl;

    @Value("${gcloud.oauth.redirect-url.attach-mail}")
    private String attachMailRedirectUrl;

    @Value("${gcloud.oauth.redirect-url.attach-calendar}")
    private String attachCalendarRedirectUrl;
}
