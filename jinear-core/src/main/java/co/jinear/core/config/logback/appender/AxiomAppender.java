package co.jinear.core.config.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import co.jinear.core.service.axiom.AxiomApiClient;
import co.jinear.core.service.axiom.request.AxiomIngestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AxiomAppender extends AppenderBase<ILoggingEvent> implements SmartLifecycle {

    @Autowired(required = false)
    private AxiomApiClient axiomApiClient;

    @Override
    protected void append(ILoggingEvent event) {
        AxiomIngestRequest axiomIngestRequest = new AxiomIngestRequest();
        axiomIngestRequest.setData(event);
        axiomApiClient.ingest(List.of(axiomIngestRequest));
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }
}

