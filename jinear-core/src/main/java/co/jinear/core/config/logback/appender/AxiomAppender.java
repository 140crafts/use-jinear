package co.jinear.core.config.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import co.jinear.core.service.axiom.AxiomApiClient;
import co.jinear.core.service.axiom.request.AxiomIngestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class AxiomAppender extends AppenderBase<ILoggingEvent> implements SmartLifecycle {

    @Autowired(required = false)
    private AxiomApiClient axiomApiClient;
    private final Queue<ILoggingEvent> logQueue = new ConcurrentLinkedQueue<>();

    @Override
    protected void append(ILoggingEvent event) {
        logQueue.add(event);
    }

    @Scheduled(fixedRate = 2000L, initialDelay = 60000L)
    public void dumpMap() {
        if (Objects.isNull(axiomApiClient) || logQueue.isEmpty()) {
            return;
        }
        List<AxiomIngestRequest> axiomIngestRequestList = logQueue
                .stream()
                .map(iLoggingEvent -> new AxiomIngestRequest(ZonedDateTime.now(), iLoggingEvent))
                .toList();
        axiomApiClient.ingest(axiomIngestRequestList);
        logQueue.clear();
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }
}

