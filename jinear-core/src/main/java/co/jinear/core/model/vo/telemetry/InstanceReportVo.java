package co.jinear.core.model.vo.telemetry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstanceReportVo {

    private String instanceId;
    private String version;
    private InstanceUsageReportVo usage;
}
