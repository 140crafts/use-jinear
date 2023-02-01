package co.jinear.core.converter.team;

import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeamInitializeVoConverter {

    TeamInitializeVo map(TeamInitializeRequest teamInitializeRequest, String currentAccountId);

    @AfterMapping
    default void afterMap(@MappingTarget TeamInitializeVo teamInitializeVo, String currentAccountId) {
        teamInitializeVo.setInitializedBy(currentAccountId);
    }
}
