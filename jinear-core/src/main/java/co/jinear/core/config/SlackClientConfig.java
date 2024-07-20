package co.jinear.core.config;

import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.SlackClientFactory;
import com.hubspot.slack.client.SlackClientRuntimeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackClientConfig {

    @Value("${slack.token}")
    private String slackToken;

    @Bean
    public SlackClient slackClient() {
        SlackClientRuntimeConfig runtimeConfig = SlackClientRuntimeConfig.builder()
                .setTokenSupplier(() -> slackToken)
                .build();
        return SlackClientFactory.defaultFactory().build(runtimeConfig);
    }
}
