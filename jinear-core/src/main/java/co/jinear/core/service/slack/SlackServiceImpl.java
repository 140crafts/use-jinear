package co.jinear.core.service.slack;

import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "slack.monitoring.impl", havingValue = "prod")
public class SlackServiceImpl implements SlackService {

    private final String MONITORING_EVENTS_CHANNEL = "jinear-events";
    private final SlackClient slackClient;

    @Async
    public void sendEventMessage(String message) {
        sendMessage(message, MONITORING_EVENTS_CHANNEL);
    }

    public void sendEventMessage(String message, Object... args) {
        sendEventMessage(String.format(message, args));
    }

    @Async
    public void sendMessage(String text, String channel) {
        slackClient.postMessage(
                ChatPostMessageParams.builder()
                        .setText(text)
                        .setChannelId(channel)
                        .build()
        ).join().unwrapOrElseThrow();
    }
}
