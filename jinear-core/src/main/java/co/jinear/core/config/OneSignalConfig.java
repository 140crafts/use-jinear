package co.jinear.core.config;

import com.onesignal.client.ApiClient;
import com.onesignal.client.api.DefaultApi;
import com.onesignal.client.auth.HttpBearerAuth;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class OneSignalConfig {

    @Value("${notification.onesignal.appId}")
    private String appId;

    @Value("${notification.onesignal.userKeyToken}")
    private String userKeyToken;

    @Bean
    public DefaultApi oneSignalApiClient() {
        ApiClient defaultClient = com.onesignal.client.Configuration.getDefaultApiClient();
        HttpBearerAuth appKey = (HttpBearerAuth) defaultClient.getAuthentication("app_key");
        appKey.setBearerToken(userKeyToken);
        HttpBearerAuth userKey = (HttpBearerAuth) defaultClient.getAuthentication("user_key");
        userKey.setBearerToken(userKeyToken);
        return new DefaultApi(defaultClient);
    }
}
