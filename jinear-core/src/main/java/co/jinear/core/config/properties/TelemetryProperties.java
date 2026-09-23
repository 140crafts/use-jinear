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
public class TelemetryProperties {

    @Value("${JINEAR_VERSION:dev}")
    private String version = "dev";

    @Value("${jinear.telemetry.update-check.enabled:false}")
    private Boolean updateCheckEnabled = Boolean.FALSE;

    @Value("${jinear.telemetry.usage-report.enabled:false}")
    private Boolean usageReportEnabled = Boolean.FALSE;

    @Value("${jinear.telemetry.url:https://api.jinear.co}")
    private String url;

    @Value("${jinear.telemetry.receiver.enabled:false}")
    private Boolean receiverEnabled = Boolean.FALSE;

    @Value("${DO_NOT_TRACK:}")
    private String doNotTrack;

    public boolean isDoNotTrackRequested() {
        return "1".equals(doNotTrack) || "true".equalsIgnoreCase(doNotTrack);
    }
}
