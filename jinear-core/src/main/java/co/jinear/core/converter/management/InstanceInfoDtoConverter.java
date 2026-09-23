package co.jinear.core.converter.management;

import co.jinear.core.model.dto.management.InstanceInfoDto;
import co.jinear.core.model.entity.management.InstanceInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstanceInfoDtoConverter {

    InstanceInfoDto map(InstanceInfo instanceInfo);
}
