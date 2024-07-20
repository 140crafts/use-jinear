package co.jinear.core.service.slack;

public interface SlackService {

    void sendEventMessage(String message);

    void sendEventMessage(String message, Object... args);
}
