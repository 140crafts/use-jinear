package co.jinear.core.converter.telemetry;

import co.jinear.core.model.request.telemetry.InstanceReportRequest;
import co.jinear.core.model.vo.telemetry.InstanceReportVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstanceReportVoConverter {

    InstanceReportVo map(InstanceReportRequest instanceReportRequest);
}
