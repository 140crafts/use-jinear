package co.jinear.core.service.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "slack.monitoring.impl", havingValue = "mock", matchIfMissing = true)
public class MockSlackService implements SlackService {

    private final String MONITORING_EVENTS_CHANNEL = "jinear-events";

    public void sendEventMessage(String message) {
        sendMessage(message, MONITORING_EVENTS_CHANNEL);
    }

    public void sendEventMessage(String message, Object... args) {
        sendEventMessage(String.format(message, args));
    }

    private void sendMessage(String text, String channel) {
        log.info(String.format("[MOCK] Slack service sending message: %s, channel: %s", text, channel));
    }
}
